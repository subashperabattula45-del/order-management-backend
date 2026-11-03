package com.subash.ordermanagement.promo.service;

import com.subash.ordermanagement.exception.DuplicatePromoCodeException;
import com.subash.ordermanagement.promo.dtos.PromoRequest;
import com.subash.ordermanagement.promo.dtos.PromoResponse;
import com.subash.ordermanagement.promo.entity.PromoCode;
import com.subash.ordermanagement.promo.repository.PromoRepository;

public class PromoService {

    private final PromoRepository promoRepository;

    public PromoService(PromoRepository promoRepository){
        this.promoRepository = promoRepository;
    }

    public PromoResponse createPromoCode(PromoRequest request){

        if(promoRepository.existsByCode(request.getCode())){
            throw new DuplicatePromoCodeException("promo code already exists" + request.getCode());
        } 

        PromoCode promocode = new PromoCode();

        promocode.setCode(request.getCode());
        promocode.setDiscountType(request.getDiscountType());
        promocode.setDiscountValue(request.getDiscountValue());
        promocode.setMinimumOrderAmount(request.getMinimumOrderAmount());
        promocode.setValidFrom(request.getValidFrom());
        promocode.setValidTo(request.getValidTo());
        promocode.setMaxUses(request.getMaxUses());
        promocode.setCurrentUses(0);
        promocode.setActive(request.getActive());

        PromoCode saved = promoRepository.save(promocode);

        return new PromoResponse(
            saved.getId(),
            saved.getCode(),
            saved.getDiscountType(),
            saved.getDiscountValue(),
            saved.getMinimumOrderAmount(),
            saved.getValidFrom(),
            saved.getValidTo(),
            saved.getMaxUses(),
            saved.getCurrentUses(),
            saved.getActive()
        );
    }
    
}
