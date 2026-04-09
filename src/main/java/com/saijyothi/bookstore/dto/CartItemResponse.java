package com.saijyothi.bookstore.dto;

import java.math.BigDecimal;

public record CartItemResponse(
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
        Integer quantity,
        BigDecimal lineTotal) {
}
