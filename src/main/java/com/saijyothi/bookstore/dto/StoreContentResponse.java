package com.saijyothi.bookstore.dto;

import java.util.List;

public record StoreContentResponse(
        List<UpcomingHighlightResponse> upcoming,
        List<UniversitySegmentResponse> universitySegments,
        List<TestimonialResponse> testimonials,
        List<CatalogueResponse> catalogues,
        List<DistributorResponse> distributors) {
}
