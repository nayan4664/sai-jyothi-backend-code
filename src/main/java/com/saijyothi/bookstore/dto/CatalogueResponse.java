package com.saijyothi.bookstore.dto;

public record CatalogueResponse(
        String title,
        String description,
        String format,
        String audience,
        String downloadLabel,
        String downloadUrl) {
}
