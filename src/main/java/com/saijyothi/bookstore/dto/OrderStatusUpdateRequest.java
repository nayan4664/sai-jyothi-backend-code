package com.saijyothi.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderStatusUpdateRequest(@NotBlank(message = "Status is required") String status) {
}
