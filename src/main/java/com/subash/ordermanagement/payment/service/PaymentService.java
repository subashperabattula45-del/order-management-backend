package com.subash.ordermanagement.payment.service;

import com.subash.ordermanagement.exception.OrderNotFoundException;
import com.subash.ordermanagement.exception.PaymentNotFoundException;
import com.subash.ordermanagement.exception.InvalidPaymentException;
import com.subash.ordermanagement.exception.InvalidPaymentStatusException;
import com.subash.ordermanagement.order.repository.OrderRepository;
import com.subash.ordermanagement.order.entity.Order;
import com.subash.ordermanagement.order.entity.OrderStatus;
import com.subash.ordermanagement.payment.dto.PaymentRequest;
import com.subash.ordermanagement.payment.dto.PaymentResponse;
import com.subash.ordermanagement.payment.dto.PaymentStatusUpdateRequest;
import com.subash.ordermanagement.payment.entity.Payment;
import com.subash.ordermanagement.payment.entity.PaymentStatus;
import com.subash.ordermanagement.payment.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

        private final PaymentRepository paymentRepository;
        private final OrderRepository orderRepository;

        public PaymentService(
                        PaymentRepository paymentRepository,
                        OrderRepository orderRepository) {

                this.paymentRepository = paymentRepository;
                this.orderRepository = orderRepository;
        }

        public PaymentResponse createPayment(
                        PaymentRequest request,
                        Long loggedInUserId) {

                Order order = orderRepository.findById(request.getOrderId())
                                .orElseThrow(() -> new OrderNotFoundException(
                                                "Order not found: " + request.getOrderId()));

                if (!order.getUser().getId().equals(loggedInUserId)) {
                        throw new InvalidPaymentException(
                                        "You cannot create a payment for this order");
                }

                if (order.getStatus() != OrderStatus.PLACED) {
                        throw new InvalidPaymentException(
                                        "Payment can only be created for a PLACED order");
                }

                boolean pendingPaymentExists = paymentRepository.existsByOrderIdAndStatus(
                                order.getId(),
                                PaymentStatus.PENDING);

                if (pendingPaymentExists) {
                        throw new InvalidPaymentException(
                                        "A pending payment already exists for this order");
                }

                Payment payment = new Payment();

                payment.setOrder(order);
                payment.setAmount(order.getTotalAmount());
                payment.setStatus(PaymentStatus.PENDING);
                payment.setTransactionId(UUID.randomUUID().toString());
                payment.setCreatedAt(LocalDateTime.now());

                Payment savedPayment = paymentRepository.save(payment);

                return new PaymentResponse(
                                savedPayment.getId(),
                                savedPayment.getOrder().getId(),
                                savedPayment.getAmount(),
                                savedPayment.getStatus(),
                                savedPayment.getTransactionId(),
                                savedPayment.getCreatedAt());
        }

        @Transactional
        public PaymentResponse updatePaymentStatus(Long id, PaymentStatusUpdateRequest request) {

                Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> new PaymentNotFoundException("payment not found with id" + id));

                PaymentStatus currentStatus = payment.getStatus();
                PaymentStatus newStatus = request.getStatus();

                if (currentStatus == PaymentStatus.PENDING) {

                        if (newStatus != PaymentStatus.SUCCESS &&
                                        newStatus != PaymentStatus.FAILED) {

                                throw new InvalidPaymentStatusException(
                                                "PENDING payment can only become SUCCESS or FAILED");
                        }

                } else if (currentStatus == PaymentStatus.SUCCESS) {

                        if (newStatus != PaymentStatus.REFUNDED) {

                                throw new InvalidPaymentStatusException(
                                                "SUCCESS payment can only become REFUNDED");
                        }

                } else {

                        throw new InvalidPaymentStatusException(
                                        "Payment status cannot be changed from " + currentStatus);
                }

                payment.setStatus(newStatus);

                if (newStatus == PaymentStatus.SUCCESS) {
                        payment.getOrder().setStatus(OrderStatus.CONFIRMED);
                }

                Payment saved = paymentRepository.save(payment);

                return new PaymentResponse(
                                saved.getId(),
                                saved.getOrder().getId(),
                                saved.getAmount(),
                                saved.getStatus(),
                                saved.getTransactionId(),
                                saved.getCreatedAt());

        }

        public PaymentResponse getPaymentById(Long paymentId, Long loggedInUserId, String role) {

                Payment payment = paymentRepository.findById(paymentId)
                                .orElseThrow(() -> new PaymentNotFoundException(
                                                "Payment not found: " + paymentId));

                if (!role.equals("ADMIN") &&
                                !payment.getOrder().getUser().getId().equals(loggedInUserId)) {

                        throw new InvalidPaymentException(
                                        "You cannot access this payment");
                }

                return new PaymentResponse(
                                payment.getId(),
                                payment.getOrder().getId(),
                                payment.getAmount(),
                                payment.getStatus(),
                                payment.getTransactionId(),
                                payment.getCreatedAt());
        }

        public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new OrderNotFoundException(
                                                "Order not found: " + orderId));

                return paymentRepository.findByOrderId(order.getId())
                                .stream()
                                .map(payment -> new PaymentResponse(
                                                payment.getId(),
                                                payment.getOrder().getId(),
                                                payment.getAmount(),
                                                payment.getStatus(),
                                                payment.getTransactionId(),
                                                payment.getCreatedAt()))
                                .toList();
        }

        public List<PaymentResponse> getPaymentsForUserOrder(
                        Long orderId,
                        Long loggedInUserId) {

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new OrderNotFoundException(
                                                "Order not found: " + orderId));

                if (!order.getUser().getId().equals(loggedInUserId)) {
                        throw new InvalidPaymentException(
                                        "You cannot access payments for this order");
                }

                return paymentRepository.findByOrderId(orderId)
                                .stream()
                                .map(payment -> new PaymentResponse(
                                                payment.getId(),
                                                payment.getOrder().getId(),
                                                payment.getAmount(),
                                                payment.getStatus(),
                                                payment.getTransactionId(),
                                                payment.getCreatedAt()))
                                .toList();
        }
}