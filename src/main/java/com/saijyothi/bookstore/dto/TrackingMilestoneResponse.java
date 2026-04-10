package com.saijyothi.bookstore.dto;

public record TrackingMilestoneResponse(
        String label,
        String description,
        boolean completed) {
}
