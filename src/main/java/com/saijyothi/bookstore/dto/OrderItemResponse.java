package com.saijyothi.bookstore.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long bookId,
        String title,
        String author,
        String category,
        BigDecimal price,
        String image,
        Integer quantity) {
}
