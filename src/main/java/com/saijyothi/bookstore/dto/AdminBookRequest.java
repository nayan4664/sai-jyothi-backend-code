package com.saijyothi.bookstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record AdminBookRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,

        @NotBlank(message = "Author is required")
        @Size(max = 255, message = "Author must be at most 255 characters")
        String author,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Price must be a valid amount")
        BigDecimal price,

        @NotBlank(message = "Category is required")
        @Size(max = 100, message = "Category must be at most 100 characters")
        String category,

        @NotNull(message = "Rating is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0")
        @Digits(integer = 2, fraction = 1, message = "Rating must have at most 1 decimal place")
        BigDecimal rating,

        @NotBlank(message = "Image URL is required")
        @Size(max = 1000, message = "Image URL must be at most 1000 characters")
        String image,

        @NotBlank(message = "Description is required")
        @Size(max = 4000, message = "Description must be at most 4000 characters")
        String description,

        @NotBlank(message = "ISBN is required")
        @Size(max = 50, message = "ISBN must be at most 50 characters")
        String isbn,

        @NotNull(message = "Pages are required")
        @Min(value = 1, message = "Pages must be at least 1")
        Integer pages,

        @NotBlank(message = "Publisher is required")
        @Size(max = 255, message = "Publisher must be at most 255 characters")
        String publisher,

        @NotBlank(message = "Language is required")
        @Size(max = 100, message = "Language must be at most 100 characters")
        String language,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be at least 0")
        Integer stock) {
}
