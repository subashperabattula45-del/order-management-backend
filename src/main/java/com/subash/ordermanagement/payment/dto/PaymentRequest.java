package com.subash.ordermanagement.payment.dto;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull
    private Long orderId;

    public PaymentRequest() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}