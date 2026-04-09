package com.saijyothi.bookstore.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record WishlistItemResponse(
        Long bookId,
        String title,
        String author,
        BigDecimal price,
        String category,
        BigDecimal rating,
        String image,
        String description,
        String isbn,
        Integer pages,
        String publisher,
        String language,
        Instant addedAt) {
}
