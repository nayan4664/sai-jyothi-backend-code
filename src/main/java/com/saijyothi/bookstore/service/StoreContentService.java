package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.CatalogueResponse;
import com.saijyothi.bookstore.dto.DistributorResponse;
import com.saijyothi.bookstore.dto.StoreContentResponse;
import com.saijyothi.bookstore.dto.TestimonialResponse;
import com.saijyothi.bookstore.dto.UniversitySegmentResponse;
import com.saijyothi.bookstore.dto.UpcomingHighlightResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StoreContentService {

    public StoreContentResponse getStoreContent() {
        return new StoreContentResponse(
                List.of(
                        new UpcomingHighlightResponse(
                                "Academic Selection Week",
                                "A guided showcase of semester-ready titles, faculty recommendations, and curated bundles for campus readers.",
                                "15-21 April 2026",
                                "Students, faculty, and institutions",
                                "See Event Details",
                                "/events"),
                        new UpcomingHighlightResponse(
                                "Summer Reading Campaign",
                                "Fresh arrivals and themed reading packs for school readers, parents, and community libraries.",
                                "Starting 25 April 2026",
                                "Schools, families, and libraries",
                                "Browse the Collection",
                                "/books?category=Kids"),
                        new UpcomingHighlightResponse(
                                "Institutional Order Window",
                                "Priority support for schools and colleges placing catalogue-based orders for the upcoming academic cycle.",
                                "Open this month",
                                "Schools, colleges, and bulk buyers",
                                "View Our Catalogues",
                                "/catalogues")),
                List.of(
                        new UniversitySegmentResponse(
                                "Engineering & Polytechnic",
                                "Core subject books, practical manuals, and exam-focused references for technical learners.",
                                "Academic",
                                "graduation-cap",
                                "Explore Engineering Titles",
                                "/books?category=Academic"),
                        new UniversitySegmentResponse(
                                "Competitive & Entrance",
                                "Objective practice books, solved papers, and strong foundation material for entrance preparation.",
                                "Competitive",
                                "briefcase",
                                "See Competitive Collection",
                                "/books?category=Competitive"),
                        new UniversitySegmentResponse(
                                "School & Junior College",
                                "Reliable titles for higher secondary learners, classroom support, and concept-building revision.",
                                "School",
                                "book-open",
                                "Browse School Titles",
                                "/books?category=School"),
                        new UniversitySegmentResponse(
                                "Commerce & Professional",
                                "Focused resources for commerce, business studies, communication, and career-ready learning.",
                                "Business",
                                "landmark",
                                "View Commerce Shelf",
                                "/books?category=Business")),
                List.of(
                        new TestimonialResponse(
                                "Dr. Meera Kulkarni",
                                "College Librarian",
                                "Nagpur",
                                5,
                                "Sai Jyothi Publication made academic sourcing far more organized for our library team. The catalogue support and regional availability saved us valuable time."),
                        new TestimonialResponse(
                                "Rohan Tiwari",
                                "Engineering Student",
                                "Amravati",
                                5,
                                "The website makes it easy to find subject books, place orders, and follow delivery updates without confusion. The experience feels simple and dependable."),
                        new TestimonialResponse(
                                "Pooja Sharma",
                                "School Coordinator",
                                "Wardha",
                                5,
                                "For institutional requirements, the catalogue section and nearby distributor details are exactly what schools and parents expect from a serious book supplier.")),
                List.of(
                        new CatalogueResponse(
                                "Academic Essentials Catalogue",
                                "A well-organized catalogue covering semester books, reference titles, and institution-friendly bundles.",
                                "PDF",
                                "Colleges, universities, and departments",
                                "View Academic Catalogue",
                                "/books?category=Academic"),
                        new CatalogueResponse(
                                "School Learning Catalogue",
                                "A focused collection for classrooms, supplementary reading, and parent-friendly recommendations.",
                                "PDF",
                                "Schools, teachers, and parents",
                                "Browse School Collection",
                                "/books?category=School"),
                        new CatalogueResponse(
                                "Competitive Preparation Catalogue",
                                "A preparation-first list of titles for aptitude, entrance, and competitive exam practice.",
                                "PDF",
                                "Aspirants and coaching centers",
                                "Explore Competitive Titles",
                                "/books?category=Competitive")),
                List.of(
                        new DistributorResponse(
                                "Nagpur",
                                "Sai Jyothi Publication Store",
                                "Main store and dispatch desk",
                                "+91 99236 93506",
                                "43HQ+PG6 Jattarodi, Indra Nagar, Nagpur, Maharashtra 440003",
                                "43HQ+PG6 jattarodi, Indra Nagar, Nagpur, Maharashtra 440003"),
                        new DistributorResponse(
                                "Amravati",
                                "Shree Book Distributors",
                                "Academic supply partner",
                                "+91 90210 11882",
                                "Rajapeth Market Road, Amravati, Maharashtra 444601",
                                "Rajapeth Market Road, Amravati, Maharashtra 444601"),
                        new DistributorResponse(
                                "Wardha",
                                "Vidya Publication Point",
                                "School and institution support",
                                "+91 97664 22115",
                                "Bachelor Road, Wardha, Maharashtra 442001",
                                "Bachelor Road, Wardha, Maharashtra 442001"),
                        new DistributorResponse(
                                "Chandrapur",
                                "Scholars Book Hub",
                                "Regional distribution partner",
                                "+91 93718 44220",
                                "Civil Lines, Chandrapur, Maharashtra 442402",
                                "Civil Lines, Chandrapur, Maharashtra 442402")));
    }
}
