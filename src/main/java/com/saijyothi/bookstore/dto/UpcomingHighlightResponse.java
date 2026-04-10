package com.saijyothi.bookstore.dto;

public record UpcomingHighlightResponse(
        String title,
        String description,
        String dateLabel,
        String audience,
        String ctaLabel,
        String ctaPath) {
}
