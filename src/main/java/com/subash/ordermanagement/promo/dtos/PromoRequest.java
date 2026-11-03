package com.subash.ordermanagement.promo.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.subash.ordermanagement.promo.entity.DiscountType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class PromoRequest {

    @NotBlank(message = "Promo code is required")
    private String code;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Discount type is required")
    private DiscountType discountType;

    @NotNull(message = "Discount value is required")
    @Positive(message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    @NotNull(message = "Minimum order amount is required")
    @PositiveOrZero(message = "Minimum order amount cannot be negative")
    private BigDecimal minimumOrderAmount;

    @NotNull(message = "Valid from is required")
    private LocalDateTime validFrom;

    @NotNull(message = "Valid to is required")
    private LocalDateTime validTo;

    @NotNull(message = "Maximum uses is required")
    @Positive(message = "Maximum uses must be greater than 0")
    private Integer maxUses;

    @NotNull(message = "Active status is required")
    private Boolean active;

    public PromoRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}