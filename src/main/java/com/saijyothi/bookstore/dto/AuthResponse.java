package com.saijyothi.bookstore.dto;

public record AuthResponse(
        String token,
        UserResponse user) {
}
