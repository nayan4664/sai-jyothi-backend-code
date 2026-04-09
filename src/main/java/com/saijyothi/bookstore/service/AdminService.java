package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.AdminBookRequest;
import com.saijyothi.bookstore.dto.AdminBookResponse;
import com.saijyothi.bookstore.dto.DashboardStatsResponse;
import com.saijyothi.bookstore.dto.OrderResponse;
import com.saijyothi.bookstore.dto.OrderStatusUpdateRequest;
import com.saijyothi.bookstore.dto.RoleUpdateRequest;
import com.saijyothi.bookstore.dto.UserResponse;
import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.entity.Book;
import com.saijyothi.bookstore.entity.Order;
import com.saijyothi.bookstore.entity.OrderStatus;
import com.saijyothi.bookstore.entity.UserRole;
import com.saijyothi.bookstore.exception.BookNotFoundException;
import com.saijyothi.bookstore.exception.OrderNotFoundException;
import com.saijyothi.bookstore.exception.UserNotFoundException;
import com.saijyothi.bookstore.repository.BookRepository;
import com.saijyothi.bookstore.repository.CartItemRepository;
import com.saijyothi.bookstore.repository.OrderRepository;
import com.saijyothi.bookstore.repository.ReviewRepository;
import com.saijyothi.bookstore.repository.UserRepository;
import com.saijyothi.bookstore.repository.WishlistItemRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final OrderService orderService;

    public AdminService(
            BookRepository bookRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            WishlistItemRepository wishlistItemRepository,
            OrderService orderService) {
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.orderService = orderService;
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalBooks = bookRepository.count();
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatusIn(List.of(OrderStatus.PLACED, OrderStatus.PROCESSING));
        BigDecimal totalRevenue = orderRepository.sumAllRevenue();

        return new DashboardStatsResponse(totalUsers, totalBooks, totalOrders, pendingOrders, totalRevenue);
    }

    @Transactional(readOnly = true)
    public List<AdminBookResponse> getBooks() {
        return bookRepository.findAllByOrderByIdAsc().stream().map(this::toAdminBookResponse).toList();
    }

    public AdminBookResponse createBook(AdminBookRequest request) {
        Book book = new Book();
        applyBookRequest(book, request);
        return toAdminBookResponse(bookRepository.save(book));
    }

    public AdminBookResponse updateBook(Long id, AdminBookRequest request) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        applyBookRequest(book, request);
        return toAdminBookResponse(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        cartItemRepository.deleteAllByBookId(id);
        reviewRepository.deleteAllByBookId(id);
        wishlistItemRepository.deleteAllByBookId(id);
        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(orderService::toResponse).toList();
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findDetailedById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(OrderStatus.valueOf(request.status().trim().toUpperCase()));
        return orderService.toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAllByOrderByIdAsc().stream().map(this::toUserResponse).toList();
    }

    public UserResponse updateUserRole(Long userId, RoleUpdateRequest request) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.setRole(UserRole.valueOf(request.role().trim().toUpperCase()));
        return toUserResponse(userRepository.save(user));
    }

    private void applyBookRequest(Book book, AdminBookRequest request) {
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setPrice(request.price());
        book.setCategory(request.category().trim());
        book.setRating(request.rating());
        book.setImage(request.image().trim());
        book.setDescription(request.description().trim());
        book.setIsbn(request.isbn().trim());
        book.setPages(request.pages());
        book.setPublisher(request.publisher().trim());
        book.setLanguage(request.language().trim());
        book.setStock(request.stock());
    }

    private AdminBookResponse toAdminBookResponse(Book book) {
        return new AdminBookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getCategory(),
                book.getRating(),
                book.getImage(),
                book.getDescription(),
                book.getIsbn(),
                book.getPages(),
                book.getPublisher(),
                book.getLanguage(),
                book.getStock(),
                book.getReviewCount());
    }

    private UserResponse toUserResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
