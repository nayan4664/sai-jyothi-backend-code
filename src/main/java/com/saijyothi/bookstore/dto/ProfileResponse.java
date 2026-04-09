package com.saijyothi.bookstore.dto;

public record ProfileResponse(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String pincode,
        String role) {
}
