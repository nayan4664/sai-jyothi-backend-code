package com.saijyothi.bookstore.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ReviewRequest(
        @NotNull(message = "Rating is required")
        @DecimalMin(value = "1.0", message = "Rating must be at least 1")
        @DecimalMax(value = "5.0", message = "Rating must be at most 5")
        @Digits(integer = 1, fraction = 1, message = "Rating must have at most 1 decimal place")
        BigDecimal rating,
        @NotBlank(message = "Comment is required")
        @Size(max = 2000, message = "Comment must be at most 2000 characters")
        String comment) {
}
