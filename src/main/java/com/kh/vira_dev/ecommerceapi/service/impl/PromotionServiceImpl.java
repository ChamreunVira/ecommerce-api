package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;
import com.kh.vira_dev.ecommerceapi.entity.Promotion;
import com.kh.vira_dev.ecommerceapi.enums.PromotionStatus;
import com.kh.vira_dev.ecommerceapi.enums.PromotionType;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.repository.PromotionRepository;
import com.kh.vira_dev.ecommerceapi.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    @Transactional
    public PromotionResult applyCoupon(String code, BigDecimal subtotal) {

        if (code == null || code.isBlank()) {
            return PromotionResult
                    .builder()
                    .promotion(null)
                    .discountAmount(BigDecimal.ZERO)
                    .freeShipping(false)
                    .build();
        }

        Promotion promotion = promotionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon code"));

        validatePromotion(promotion, subtotal);

        BigDecimal discount = calculateDiscount(promotion, subtotal);
        boolean freeShipping = promotion.getType() == PromotionType.FREE_SHIPPING;

        promotion.setUsageCount(
                (promotion.getUsageCount() == null ? 0 : promotion.getUsageCount()) + 1
        );
        promotionRepository.save(promotion);

        return PromotionResult
                .builder()
                .promotion(promotion)
                .discountAmount(discount)
                .freeShipping(freeShipping)
                .build();
    }

    void validatePromotion(Promotion promotion, BigDecimal subtotal) {
        if (promotion.getStatus() != PromotionStatus.ACTIVE) {
            throw new IllegalStateException("Coupon code not found");
        }
        if (promotion.getExpiryAt() != null && promotion.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon code is expired.");
        }
        if (promotion.getStartAt() != null && promotion.getStartAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon is not active yet.");
        }
        if (promotion.getUsageLimit() != null
                && promotion.getUsageCount() != null
                && promotion.getUsageCount() >= promotion.getUsageLimit()) {
            throw new IllegalStateException("Coupon usage limit reached.");
        }
        if (promotion.getMinimumOrder() != null
                && subtotal.compareTo(promotion.getMinimumOrder()) < 0) {
            throw new IllegalStateException("Minimum order is " + promotion.getMinimumOrder());
        }
    }

    private BigDecimal calculateDiscount(Promotion promotion, BigDecimal subtotal) {
        BigDecimal discount = switch (promotion.getType()) {
            case PERCENTAGE -> subtotal
                    .multiply(promotion.getValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            case FIXED_AMOUNT -> promotion.getValue();
            case FREE_SHIPPING -> BigDecimal.ZERO;
        };

        if (discount == null) {
            return BigDecimal.ZERO;
        }
        if (discount.compareTo(subtotal) > 0) {
            return subtotal;
        }
        return discount;
    }

}
