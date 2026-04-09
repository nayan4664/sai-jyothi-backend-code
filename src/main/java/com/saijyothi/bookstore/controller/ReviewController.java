package com.saijyothi.bookstore.controller;

import com.saijyothi.bookstore.dto.ReviewRequest;
import com.saijyothi.bookstore.dto.ReviewResponse;
import com.saijyothi.bookstore.security.UserPrincipal;
import com.saijyothi.bookstore.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewResponse> getReviews(@PathVariable Long bookId) {
        return reviewService.getReviews(bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewRequest request) {
        return reviewService.createOrUpdateReview(userPrincipal.getId(), bookId, request);
    }

    @PutMapping("/{reviewId}")
    public ReviewResponse updateReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {
        return reviewService.updateReview(userPrincipal.getId(), reviewId, request);
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(userPrincipal.getId(), reviewId);
    }
}
