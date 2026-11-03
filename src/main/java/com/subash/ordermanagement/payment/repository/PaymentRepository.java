package com.subash.ordermanagement.payment.repository;

import com.subash.ordermanagement.payment.entity.Payment;
import com.subash.ordermanagement.payment.entity.PaymentStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderId(Long orderId);

    boolean existsByOrderIdAndStatus(
        Long orderId,
        PaymentStatus status
);
}