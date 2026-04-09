package com.saijyothi.bookstore.repository;

import com.saijyothi.bookstore.entity.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @EntityGraph(attributePaths = {"book"})
    List<CartItem> findAllByUserIdOrderByIdAsc(Long userId);

    @EntityGraph(attributePaths = {"book"})
    Optional<CartItem> findByUserIdAndBookId(Long userId, Long bookId);

    void deleteAllByUserId(Long userId);

    void deleteAllByBookId(Long bookId);
}
