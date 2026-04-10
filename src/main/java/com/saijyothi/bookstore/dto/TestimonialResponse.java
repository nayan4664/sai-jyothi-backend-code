package com.saijyothi.bookstore.dto;

public record TestimonialResponse(
        String name,
        String role,
        String city,
        int rating,
        String quote) {
}
