package com.subash.ordermanagement.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.subash.ordermanagement.order.dto.OrderRequest;
import com.subash.ordermanagement.order.dto.OrderResponse;
import com.subash.ordermanagement.order.dto.OrderStatusUpdateRequest;
import com.subash.ordermanagement.order.service.OrderService;
import java.util.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        Long loggedInUserId = jwt.getClaim("userId");

        request.setUserId(loggedInUserId);

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Long loggedInUserId = jwt.getClaim("userId");
        String role = jwt.getClaim("role");

        return ResponseEntity.ok(
                orderService.getOrderById(
                        id,
                        loggedInUserId,
                        role));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> orders = orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(
            @PathVariable Long userId) {

        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {

        OrderResponse updated = orderService.updateOrderStatus(id, request);

        return ResponseEntity.ok(updated);

    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal Jwt jwt) {

        Long loggedInUserId = jwt.getClaim("userId");

        return ResponseEntity.ok(
                orderService.getOrdersByUserId(loggedInUserId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelMyOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Long loggedInUserId = jwt.getClaim("userId");

        return ResponseEntity.ok(
                orderService.cancelOrder(id, loggedInUserId));
    }

}