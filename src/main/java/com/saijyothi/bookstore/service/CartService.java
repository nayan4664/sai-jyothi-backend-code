package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.CartItemRequest;
import com.saijyothi.bookstore.dto.CartItemResponse;
import com.saijyothi.bookstore.dto.CartResponse;
import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.entity.Book;
import com.saijyothi.bookstore.entity.CartItem;
import com.saijyothi.bookstore.exception.BookOutOfStockException;
import com.saijyothi.bookstore.exception.BookNotFoundException;
import com.saijyothi.bookstore.exception.InsufficientStockException;
import com.saijyothi.bookstore.exception.UserNotFoundException;
import com.saijyothi.bookstore.repository.BookRepository;
import com.saijyothi.bookstore.repository.CartItemRepository;
import com.saijyothi.bookstore.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CartService(
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public CartResponse getCart(Long userId) {
        AppUser user = getUser(userId);
        List<CartItemResponse> items = cartItemRepository.findAllByUserIdOrderByIdAsc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();

        return toCartResponse(user.getId(), items);
    }

    public CartResponse addOrUpdateItem(Long userId, CartItemRequest request) {
        AppUser user = getUser(userId);
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new BookNotFoundException(request.bookId()));

        if (book.getStock() <= 0) {
            throw new BookOutOfStockException(book.getTitle());
        }

        if (request.quantity() > book.getStock()) {
            throw new InsufficientStockException(book.getTitle());
        }

        CartItem cartItem = cartItemRepository.findByUserIdAndBookId(userId, request.bookId())
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setUser(user);
                    item.setBook(book);
                    return item;
                });

        cartItem.setQuantity(request.quantity());
        cartItemRepository.save(cartItem);

        return getCart(userId);
    }

    public CartResponse removeItem(Long userId, Long bookId) {
        getUser(userId);
        cartItemRepository.findByUserIdAndBookId(userId, bookId).ifPresent(cartItemRepository::delete);
        return getCart(userId);
    }

    public void clearCart(Long userId) {
        getUser(userId);
        cartItemRepository.deleteAllByUserId(userId);
    }

    private AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private CartItemResponse toResponse(CartItem cartItem) {
        Book book = cartItem.getBook();
        BigDecimal lineTotal = book.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return new CartItemResponse(
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
                cartItem.getQuantity(),
                lineTotal);
    }

    private CartResponse toCartResponse(Long userId, List<CartItemResponse> items) {
        int itemCount = items.stream().mapToInt(CartItemResponse::quantity).sum();
        BigDecimal subtotal = items.stream()
                .map(CartItemResponse::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(userId, items, itemCount, subtotal);
    }
}
