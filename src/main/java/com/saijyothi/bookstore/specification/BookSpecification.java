package com.saijyothi.bookstore.specification;

import com.saijyothi.bookstore.entity.Book;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

public final class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> filter(
            String search,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            BigDecimal minRating) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String searchValue = "%" + search.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchValue),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), searchValue),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), searchValue),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchValue)));
            }

            if (category != null && !category.isBlank() && !"all".equalsIgnoreCase(category.trim())) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category.trim()));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (minRating != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
