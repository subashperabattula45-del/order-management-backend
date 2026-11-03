package com.subash.ordermanagement.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.subash.ordermanagement.order.entity.OrderStatus;

public class OrderResponse {

    private Long id;
    private Long userId;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private String promoCode;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public OrderResponse() {
    }

    public OrderResponse(
            Long id,
            Long userId,
            OrderStatus status,
            BigDecimal subtotal,
            BigDecimal discountAmount,
            BigDecimal totalAmount,
            String promoCode,
            LocalDateTime createdAt,
            List<OrderItemResponse> items) {

        this.id = id;
        this.userId = userId;
        this.status = status;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.promoCode = promoCode;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}