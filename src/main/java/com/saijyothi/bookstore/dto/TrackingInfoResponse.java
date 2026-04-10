package com.saijyothi.bookstore.dto;

import java.util.List;

public record TrackingInfoResponse(
        String trackingCode,
        String currentLabel,
        String estimatedDeliveryText,
        String carrier,
        String trackingAddress,
        List<TrackingMilestoneResponse> milestones) {
}
