package com.saijyothi.bookstore.dto;

import java.math.BigDecimal;

public record DashboardStatsResponse(
        long totalUsers,
        long totalBooks,
        long totalOrders,
        long pendingOrders,
        BigDecimal totalRevenue) {
}
