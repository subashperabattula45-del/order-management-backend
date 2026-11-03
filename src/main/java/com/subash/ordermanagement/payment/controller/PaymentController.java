package com.subash.ordermanagement.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.subash.ordermanagement.payment.dto.PaymentRequest;
import com.subash.ordermanagement.payment.dto.PaymentResponse;
import com.subash.ordermanagement.payment.dto.PaymentStatusUpdateRequest;
import com.subash.ordermanagement.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        Long loggedInUserId = jwt.getClaim("userId");

        PaymentResponse response = paymentService.createPayment(
                request,
                loggedInUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable Long id,
            @Valid @RequestBody PaymentStatusUpdateRequest request) {

        PaymentResponse response = paymentService.updatePaymentStatus(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal Jwt jwt) {

        Long loggedInUserId = jwt.getClaim("userId");
        String role = jwt.getClaim("role");

        return ResponseEntity.ok(
                paymentService.getPaymentById(
                        paymentId,
                        loggedInUserId,
                        role));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrderId(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByOrderId(orderId));
    }

    @GetMapping("/my-order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getMyOrderPayments(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {

        Long loggedInUserId = jwt.getClaim("userId");

        return ResponseEntity.ok(
                paymentService.getPaymentsForUserOrder(
                        orderId,
                        loggedInUserId));
    }

}
