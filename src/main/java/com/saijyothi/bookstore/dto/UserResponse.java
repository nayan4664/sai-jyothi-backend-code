package com.saijyothi.bookstore.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role) {
}
