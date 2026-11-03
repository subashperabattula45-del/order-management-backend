package com.subash.ordermanagement.promo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subash.ordermanagement.promo.entity.PromoCode;

public interface PromoRepository extends JpaRepository<PromoCode, Long> {

    Optional<PromoCode> findByCode(String code);

    boolean existsByCode(String code);
}