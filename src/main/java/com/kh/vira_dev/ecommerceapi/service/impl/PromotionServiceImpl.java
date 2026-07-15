package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.PromotionRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;
import com.kh.vira_dev.ecommerceapi.entity.Promotion;
import com.kh.vira_dev.ecommerceapi.enums.PromotionStatus;
import com.kh.vira_dev.ecommerceapi.enums.PromotionType;
import com.kh.vira_dev.ecommerceapi.exception.DuplicateResourceException;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.PromotionMapper;
import com.kh.vira_dev.ecommerceapi.repository.PromotionRepository;
import com.kh.vira_dev.ecommerceapi.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    @Transactional
    public PromotionResponse create(PromotionRequest request) {
        validateDates(request);
        String code = request.getCode().trim().toUpperCase();
        if (promotionRepository.existsByCodeIgnoreCase(code)) {
            throw new DuplicateResourceException("Promotion");
        }

        Promotion promotion = promotionMapper.toEntity(request);
        Promotion saved = promotionRepository.save(promotion);
        log.info("Created promotion: {}", saved.getCode());
        return promotionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PromotionResponse update(Long id, PromotionRequest request) {
        validateDates(request);
        Promotion promotion = findByOrThrow(id);

        String code = request.getCode().trim().toUpperCase();
        if (promotionRepository.existsByCodeIgnoreCaseAndIdNot(code, id)) {
            throw new DuplicateResourceException("Promotion");
        }

        promotionMapper.applyPromotionFields(promotion, request);
        Promotion saved = promotionRepository.save(promotion);
        log.info("Updated promotion: {}", saved.getCode());
        return promotionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Promotion promotion = findByOrThrow(id);
        if (promotion.getOrders() != null && !promotion.getOrders().isEmpty()) {
            throw new IllegalStateException("Cannot delete promotion that is used by orders.");
        }
        log.info("Deleted promotion: {}", promotion.getCode());
        promotionRepository.delete(promotion);
    }

    @Override
    public PromotionResponse getById(Long id) {
        return promotionMapper.toResponse(findByOrThrow(id));
    }

    @Override
    public List<PromotionResponse> getAll() {
        return promotionMapper.toResponseList(promotionRepository.findAll());
    }

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

        Promotion promotion = promotionRepository.findByCodeIgnoreCase(code.trim())
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
            throw new IllegalStateException("Coupon is not active.");
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
            case PERCENTAGE -> {
                if (promotion.getValue() == null) {
                    yield BigDecimal.ZERO;
                }
                yield subtotal
                        .multiply(promotion.getValue())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            }
            case FIXED_AMOUNT -> promotion.getValue() == null ? BigDecimal.ZERO : promotion.getValue();
            case FREE_SHIPPING -> BigDecimal.ZERO;
        };

        if (discount.compareTo(subtotal) > 0) {
            return subtotal;
        }
        return discount;
    }

    private void validateDates(PromotionRequest request) {
        if (request.getStartAt() != null
                && request.getExpiryAt() != null
                && request.getExpiryAt().isBefore(request.getStartAt())) {
            throw new IllegalArgumentException("expiryAt must be after startAt.");
        }
    }

    private Promotion findByOrThrow(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion"));
    }

}
