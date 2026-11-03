package com.subash.ordermanagement.promo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.subash.ordermanagement.promo.dtos.PromoRequest;
import com.subash.ordermanagement.promo.dtos.PromoResponse;
import com.subash.ordermanagement.promo.service.PromoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/promos")
public class PromoController {

    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    @PostMapping
    public ResponseEntity<PromoResponse> createPromoCode(
            @Valid @RequestBody PromoRequest request) {

        PromoResponse response = promoService.createPromoCode(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}