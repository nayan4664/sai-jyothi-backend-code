package com.saijyothi.bookstore.controller;

import com.saijyothi.bookstore.dto.WishlistItemResponse;
import com.saijyothi.bookstore.security.UserPrincipal;
import com.saijyothi.bookstore.service.WishlistService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<WishlistItemResponse> getWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return wishlistService.getWishlist(userPrincipal.getId());
    }

    @PostMapping("/{bookId}")
    public List<WishlistItemResponse> addItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long bookId) {
        return wishlistService.addItem(userPrincipal.getId(), bookId);
    }

    @DeleteMapping("/{bookId}")
    public List<WishlistItemResponse> removeItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long bookId) {
        return wishlistService.removeItem(userPrincipal.getId(), bookId);
    }
}
