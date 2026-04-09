package com.saijyothi.bookstore.controller;

import com.saijyothi.bookstore.dto.AdminBookRequest;
import com.saijyothi.bookstore.dto.AdminBookResponse;
import com.saijyothi.bookstore.dto.DashboardStatsResponse;
import com.saijyothi.bookstore.dto.OrderResponse;
import com.saijyothi.bookstore.dto.OrderStatusUpdateRequest;
import com.saijyothi.bookstore.dto.RoleUpdateRequest;
import com.saijyothi.bookstore.dto.UserResponse;
import com.saijyothi.bookstore.service.AdminService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public DashboardStatsResponse getDashboardStats() {
        return adminService.getDashboardStats();
    }

    @GetMapping("/books")
    public List<AdminBookResponse> getBooks() {
        return adminService.getBooks();
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminBookResponse createBook(@Valid @RequestBody AdminBookRequest request) {
        return adminService.createBook(request);
    }

    @PutMapping("/books/{id}")
    public AdminBookResponse updateBook(@PathVariable Long id, @Valid @RequestBody AdminBookRequest request) {
        return adminService.updateBook(id, request);
    }

    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        adminService.deleteBook(id);
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return adminService.getOrders();
    }

    @PutMapping("/orders/{orderId}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        return adminService.updateOrderStatus(orderId, request);
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return adminService.getUsers();
    }

    @PutMapping("/users/{userId}/role")
    public UserResponse updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody RoleUpdateRequest request) {
        return adminService.updateUserRole(userId, request);
    }
}
