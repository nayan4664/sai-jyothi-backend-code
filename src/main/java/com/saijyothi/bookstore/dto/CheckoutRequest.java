package com.saijyothi.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CheckoutRequest(
        @NotBlank(message = "Full name is required")
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z .'-]{1,254}$",
                message = "Enter a valid full name")
        @Size(max = 255, message = "Full name must be at most 255 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,

        @NotBlank(message = "Phone is required")
        @Pattern(
                regexp = "^(\\+91[- ]?)?[6-9]\\d{9}$",
                message = "Enter a valid 10-digit Indian phone number")
        @Size(max = 50, message = "Phone must be at most 50 characters")
        String phone,

        @NotBlank(message = "Address is required")
        @Size(min = 10, max = 1000, message = "Address must be between 10 and 1000 characters")
        String address,

        @NotBlank(message = "City is required")
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z .'-]{1,254}$",
                message = "Enter a valid city name")
        @Size(max = 255, message = "City must be at most 255 characters")
        String city,

        @NotBlank(message = "State is required")
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z .'-]{1,254}$",
                message = "Enter a valid state name")
        @Size(max = 255, message = "State must be at most 255 characters")
        String state,

        @NotBlank(message = "Pincode is required")
        @Pattern(regexp = "^\\d{6}$", message = "PIN code must be exactly 6 digits")
        @Size(max = 20, message = "Pincode must be at most 20 characters")
        String pincode,

        @NotBlank(message = "Payment method is required")
        @Size(max = 50, message = "Payment method must be at most 50 characters")
        String paymentMethod) {
}
