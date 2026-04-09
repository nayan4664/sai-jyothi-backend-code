package com.saijyothi.bookstore.repository;

import com.saijyothi.bookstore.entity.Review;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = "user")
    List<Review> findAllByBookIdOrderByCreatedAtDesc(Long bookId);

    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);

    Optional<Review> findByIdAndUserId(Long id, Long userId);

    long countByBookId(Long bookId);

    @Query("select coalesce(avg(r.rating), 0) from Review r where r.book.id = :bookId")
    BigDecimal findAverageRatingByBookId(Long bookId);

    void deleteAllByBookId(Long bookId);
}
