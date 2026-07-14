package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;
import com.kh.vira_dev.ecommerceapi.entity.Promotion;
import com.kh.vira_dev.ecommerceapi.enums.PromotionStatus;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.repository.PromotionRepository;
import com.kh.vira_dev.ecommerceapi.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public PromotionResult applyCoupon(String code, BigDecimal subtotal) {

        if(code == null || code.isBlank()) {
            return PromotionResult
                    .builder()
                    .promotion(null)
                    .discountAmount(BigDecimal.ZERO)
                    .build();
        }

        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon code"));

        validatePromotion(promotion, subtotal);

        BigDecimal discount = calculateDiscount(promotion, subtotal);

        return PromotionResult
                .builder()
                .promotion(promotion)
                .discountAmount(discount)
                .build();
    }

    void validatePromotion(Promotion promotion, BigDecimal subtotal) {
        if(promotion.getStatus() != PromotionStatus.ACTIVE) {
            throw new IllegalStateException("Coupon code not found");
        }
        if(promotion.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon code is expired.");
        }
        if(promotion.getStartAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon start time is expired.");
        }
        if(promotion.getUsageCount() < promotion.getUsageLimit()) {
            throw new IllegalStateException("Coupon usage limit reached.");
        }
        if(subtotal.compareTo(promotion.getMinimumOrder()) < 0) {
            throw new IllegalStateException("Minimum order is " + promotion.getMinimumOrder());
        }
    }


    private BigDecimal calculateDiscount(Promotion promotion , BigDecimal subtotal) {
        return switch (promotion.getType()) {
            case PERCENTAGE -> subtotal
                    .multiply(promotion.getValue())
                    .divide(BigDecimal.valueOf(100));
            case FIXED_AMOUNT -> promotion.getValue();
            case FREE_SHIPPING -> BigDecimal.ZERO;
        };
    }

}
