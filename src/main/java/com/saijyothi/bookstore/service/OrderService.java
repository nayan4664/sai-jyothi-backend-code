package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.CheckoutRequest;
import com.saijyothi.bookstore.dto.CustomerInfoResponse;
import com.saijyothi.bookstore.dto.OrderItemResponse;
import com.saijyothi.bookstore.dto.OrderResponse;
import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.entity.CartItem;
import com.saijyothi.bookstore.entity.Order;
import com.saijyothi.bookstore.entity.OrderItem;
import com.saijyothi.bookstore.entity.OrderStatus;
import com.saijyothi.bookstore.exception.BookOutOfStockException;
import com.saijyothi.bookstore.exception.EmptyCartException;
import com.saijyothi.bookstore.exception.InsufficientStockException;
import com.saijyothi.bookstore.exception.OrderNotFoundException;
import com.saijyothi.bookstore.exception.UserNotFoundException;
import com.saijyothi.bookstore.repository.CartItemRepository;
import com.saijyothi.bookstore.repository.OrderRepository;
import com.saijyothi.bookstore.repository.UserRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public OrderResponse checkout(Long userId, CheckoutRequest request) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<CartItem> cartItems = cartItemRepository.findAllByUserIdOrderByIdAsc(userId);
        if (cartItems.isEmpty()) {
            throw new EmptyCartException();
        }

        cartItems.forEach(cartItem -> {
            if (cartItem.getBook().getStock() <= 0) {
                throw new BookOutOfStockException(cartItem.getBook().getTitle());
            }
            if (cartItem.getQuantity() > cartItem.getBook().getStock()) {
                throw new InsufficientStockException(cartItem.getBook().getTitle());
            }
        });

        BigDecimal subtotal = cartItems.stream()
                .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal shippingCost = subtotal.compareTo(BigDecimal.valueOf(500)) >= 0 ? BigDecimal.ZERO : BigDecimal.valueOf(50);
        BigDecimal total = subtotal.add(shippingCost);

        Order order = new Order();
        order.setOrderNumber("ORD" + Instant.now().toEpochMilli());
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setSubtotal(subtotal);
        order.setShippingCost(shippingCost);
        order.setTotal(total);
        order.setFullName(request.fullName().trim());
        order.setEmail(request.email().trim());
        order.setPhone(request.phone().trim());
        order.setAddress(request.address().trim());
        order.setCity(request.city().trim());
        order.setState(request.state().trim());
        order.setPincode(request.pincode().trim());
        order.setPaymentMethod(request.paymentMethod().trim());

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBookId(cartItem.getBook().getId());
            item.setTitle(cartItem.getBook().getTitle());
            item.setAuthor(cartItem.getBook().getAuthor());
            item.setCategory(cartItem.getBook().getCategory());
            item.setPrice(cartItem.getBook().getPrice());
            item.setImage(cartItem.getBook().getImage());
            item.setQuantity(cartItem.getQuantity());
            return item;
        }).toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartItems.forEach(cartItem -> cartItem.getBook().setStock(cartItem.getBook().getStock() - cartItem.getQuantity()));
        cartItemRepository.deleteAllByUserId(userId);
        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(Long userId) {
        return orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return toResponse(order);
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(item ->
                new OrderItemResponse(
                        item.getBookId(),
                        item.getTitle(),
                        item.getAuthor(),
                        item.getCategory(),
                        item.getPrice(),
                        item.getImage(),
                        item.getQuantity()))
                .toList();

        CustomerInfoResponse customerInfo = new CustomerInfoResponse(
                order.getFullName(),
                order.getEmail(),
                order.getPhone(),
                order.getAddress(),
                order.getCity(),
                order.getState(),
                order.getPincode(),
                order.getPaymentMethod());

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getTotal(),
                order.getShippingCost(),
                order.getCreatedAt(),
                customerInfo,
                items);
    }
}
