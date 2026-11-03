package com.subash.ordermanagement.promo.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.subash.ordermanagement.promo.entity.DiscountType;

public class PromoResponse {

    private Long id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minimumOrderAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer maxUses;
    private Integer currentUses;
    private Boolean active;

    public PromoResponse() {
    }

    public PromoResponse(
            Long id,
            String code,
            DiscountType discountType,
            BigDecimal discountValue,
            BigDecimal minimumOrderAmount,
            LocalDateTime validFrom,
            LocalDateTime validTo,
            Integer maxUses,
            Integer currentUses,
            Boolean active) {

        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumOrderAmount = minimumOrderAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.maxUses = maxUses;
        this.currentUses = currentUses;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public Integer getCurrentUses() {
        return currentUses;
    }

    public Boolean getActive() {
        return active;
    }
}