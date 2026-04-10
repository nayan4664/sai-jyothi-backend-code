package com.saijyothi.bookstore.dto;

public record UniversitySegmentResponse(
        String name,
        String description,
        String category,
        String icon,
        String ctaLabel,
        String ctaPath) {
}
