package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.ReviewRequest;
import com.saijyothi.bookstore.dto.ReviewResponse;
import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.entity.Book;
import com.saijyothi.bookstore.entity.Review;
import com.saijyothi.bookstore.exception.BookNotFoundException;
import com.saijyothi.bookstore.exception.ReviewNotFoundException;
import com.saijyothi.bookstore.exception.UserNotFoundException;
import com.saijyothi.bookstore.repository.BookRepository;
import com.saijyothi.bookstore.repository.ReviewRepository;
import com.saijyothi.bookstore.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long bookId) {
        getBook(bookId);
        return reviewRepository.findAllByBookIdOrderByCreatedAtDesc(bookId).stream().map(this::toResponse).toList();
    }

    public ReviewResponse createOrUpdateReview(Long userId, Long bookId, ReviewRequest request) {
        AppUser user = getUser(userId);
        Book book = getBook(bookId);

        Review review = reviewRepository.findByBookIdAndUserId(bookId, userId).orElseGet(() -> {
            Review newReview = new Review();
            newReview.setBook(book);
            newReview.setUser(user);
            return newReview;
        });

        review.setRating(request.rating());
        review.setComment(request.comment().trim());
        Review savedReview = reviewRepository.save(review);
        refreshBookReviewStats(book);
        return toResponse(savedReview);
    }

    public ReviewResponse updateReview(Long userId, Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        review.setRating(request.rating());
        review.setComment(request.comment().trim());
        Review savedReview = reviewRepository.save(review);
        refreshBookReviewStats(review.getBook());
        return toResponse(savedReview);
    }

    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        Book book = review.getBook();
        reviewRepository.delete(review);
        refreshBookReviewStats(book);
    }

    private void refreshBookReviewStats(Book book) {
        long count = reviewRepository.countByBookId(book.getId());
        BigDecimal average = reviewRepository.findAverageRatingByBookId(book.getId());
        book.setReviewCount((int) count);
        book.setRating(count == 0 ? BigDecimal.ZERO : average);
        bookRepository.save(book);
    }

    private AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Book getBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUser().getId(),
                review.getUser().getName());
    }
}
