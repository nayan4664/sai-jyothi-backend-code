package com.saijyothi.bookstore.controller;

import com.saijyothi.bookstore.dto.CheckoutRequest;
import com.saijyothi.bookstore.dto.OrderResponse;
import com.saijyothi.bookstore.security.UserPrincipal;
import com.saijyothi.bookstore.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public OrderResponse checkout(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CheckoutRequest request) {
        return orderService.checkout(userPrincipal.getId(), request);
    }

    @GetMapping
    public List<OrderResponse> getOrders(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return orderService.getOrders(userPrincipal.getId());
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long orderId) {
        return orderService.getOrder(userPrincipal.getId(), orderId);
    }
}
