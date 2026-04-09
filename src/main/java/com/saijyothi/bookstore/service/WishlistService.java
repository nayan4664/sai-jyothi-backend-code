package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.WishlistItemResponse;
import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.entity.Book;
import com.saijyothi.bookstore.entity.WishlistItem;
import com.saijyothi.bookstore.exception.BookNotFoundException;
import com.saijyothi.bookstore.exception.UserNotFoundException;
import com.saijyothi.bookstore.repository.BookRepository;
import com.saijyothi.bookstore.repository.UserRepository;
import com.saijyothi.bookstore.repository.WishlistItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public WishlistService(
            WishlistItemRepository wishlistItemRepository,
            UserRepository userRepository,
            BookRepository bookRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<WishlistItemResponse> getWishlist(Long userId) {
        getUser(userId);
        return wishlistItemRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<WishlistItemResponse> addItem(Long userId, Long bookId) {
        AppUser user = getUser(userId);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        wishlistItemRepository.findByUserIdAndBookId(userId, bookId).orElseGet(() -> {
            WishlistItem item = new WishlistItem();
            item.setUser(user);
            item.setBook(book);
            return wishlistItemRepository.save(item);
        });

        return getWishlist(userId);
    }

    public List<WishlistItemResponse> removeItem(Long userId, Long bookId) {
        getUser(userId);
        wishlistItemRepository.deleteByUserIdAndBookId(userId, bookId);
        return getWishlist(userId);
    }

    private AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private WishlistItemResponse toResponse(WishlistItem wishlistItem) {
        Book book = wishlistItem.getBook();
        return new WishlistItemResponse(
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
                wishlistItem.getCreatedAt());
    }
}
