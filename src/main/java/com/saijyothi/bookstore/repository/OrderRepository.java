package com.saijyothi.bookstore.repository;

import com.saijyothi.bookstore.entity.Order;
import com.saijyothi.bookstore.entity.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"items"})
    List<Order> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"items"})
    List<Order> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findDetailedById(Long id);

    long countByStatusIn(List<OrderStatus> statuses);

    @Query("select coalesce(sum(o.total), 0) from Order o")
    BigDecimal sumAllRevenue();
}
