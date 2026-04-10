package com.saijyothi.bookstore.dto;

public record DistributorResponse(
        String city,
        String partnerName,
        String contactPerson,
        String phone,
        String address,
        String mapQuery) {
}
