package com.saijyothi.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long userId,
        List<CartItemResponse> items,
        Integer itemCount,
        BigDecimal subtotal) {
}
