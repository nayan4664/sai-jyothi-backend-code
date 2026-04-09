package com.saijyothi.bookstore.dto;

public record CustomerInfoResponse(
        String fullName,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String pincode,
        String paymentMethod) {
}
