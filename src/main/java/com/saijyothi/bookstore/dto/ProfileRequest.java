package com.saijyothi.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must be at most 255 characters")
        String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,
        @Size(max = 50, message = "Phone must be at most 50 characters")
        String phone,
        @Size(max = 1000, message = "Address must be at most 1000 characters")
        String address,
        @Size(max = 255, message = "City must be at most 255 characters")
        String city,
        @Size(max = 255, message = "State must be at most 255 characters")
        String state,
        @Size(max = 20, message = "Pincode must be at most 20 characters")
        String pincode) {
}
