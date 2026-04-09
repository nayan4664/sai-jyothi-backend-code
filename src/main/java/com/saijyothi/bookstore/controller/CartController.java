package com.saijyothi.bookstore.controller;

import com.saijyothi.bookstore.dto.CartItemRequest;
import com.saijyothi.bookstore.dto.CartResponse;
import com.saijyothi.bookstore.security.UserPrincipal;
import com.saijyothi.bookstore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return cartService.getCart(userPrincipal.getId());
    }

    @PostMapping("/items")
    public CartResponse addOrUpdateItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CartItemRequest request) {
        return cartService.addOrUpdateItem(userPrincipal.getId(), request);
    }

    @DeleteMapping("/items/{bookId}")
    public CartResponse removeItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long bookId) {
        return cartService.removeItem(userPrincipal.getId(), bookId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        cartService.clearCart(userPrincipal.getId());
    }
}
