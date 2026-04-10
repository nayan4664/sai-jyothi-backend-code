package com.saijyothi.bookstore.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderId,
        String status,
        BigDecimal total,
        BigDecimal shippingCost,
        Instant orderDate,
        CustomerInfoResponse customerInfo,
        List<OrderItemResponse> items,
        TrackingInfoResponse tracking) {
}
