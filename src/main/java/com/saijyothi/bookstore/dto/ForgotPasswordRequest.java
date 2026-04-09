package com.saijyothi.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,

        @NotBlank(message = "New password is required")
        @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{6,255}$",
                message = "Password must contain at least one letter and one number")
        String newPassword) {
}
