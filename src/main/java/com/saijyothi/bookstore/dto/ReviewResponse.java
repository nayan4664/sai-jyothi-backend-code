package com.saijyothi.bookstore.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ReviewResponse(
        Long id,
        BigDecimal rating,
        String comment,
        Instant createdAt,
        Long userId,
        String userName) {
}
