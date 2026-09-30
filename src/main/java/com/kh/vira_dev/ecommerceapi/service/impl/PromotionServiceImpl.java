package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.PromotionRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;
import com.kh.vira_dev.ecommerceapi.entity.Promotion;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.enums.PromotionStatus;
import com.kh.vira_dev.ecommerceapi.enums.PromotionType;
import com.kh.vira_dev.ecommerceapi.exception.DuplicateResourceException;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.PromotionMapper;
import com.kh.vira_dev.ecommerceapi.repository.OrderRepository;
import com.kh.vira_dev.ecommerceapi.repository.PromotionRepository;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
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
    private final OrderRepository orderRepository;
    private final AuthService authService;

    // ─── CRUD ────────────────────────────────────────────────────────────────

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
    public PromotionResponse updateStatus(Long id, String status) {
        validateStatus(status);
        Promotion promotion = findByOrThrow(id);
        promotion.setStatus(PromotionStatus.valueOf(status.toUpperCase()));
        return promotionMapper.toResponse(promotionRepository.save(promotion));
    }

    @Override
    public PromotionResponse getById(Long id) {
        return promotionMapper.toResponse(findByOrThrow(id));
    }

    @Override
    public List<PromotionResponse> getAll() {
        return promotionMapper.toResponseList(promotionRepository.findAll());
    }

    // ─── Coupon validation ────────────────────────────────────────────────────

    /**
     * Preview-only validation used by the "Apply" button.
     * Runs all 7 checks WITHOUT incrementing usageCount.
     */
    @Override
    public PromotionResult validateCoupon(String code, BigDecimal subtotal) {
        if (code == null || code.isBlank()) {
            return emptyResult();
        }

        Promotion promotion = promotionRepository.findByCodeIgnoreCase(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon code"));

        User currentUser = authService.authenticated();
        runAllChecks(promotion, subtotal, currentUser);

        BigDecimal discount = calculateDiscount(promotion, subtotal);
        boolean freeShipping = promotion.getType() == PromotionType.FREE_SHIPPING;

        return PromotionResult.builder()
                .promotion(promotion)
                .discountAmount(discount)
                .freeShipping(freeShipping)
                .build();
    }

    /**
     * Called at actual checkout: validates and then increments usageCount.
     */
    @Override
    @Transactional
    public PromotionResult applyCoupon(String code, BigDecimal subtotal) {
        if (code == null || code.isBlank()) {
            return emptyResult();
        }

        Promotion promotion = promotionRepository.findByCodeIgnoreCase(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon code"));

        User currentUser = authService.authenticated();
        runAllChecks(promotion, subtotal, currentUser);

        BigDecimal discount = calculateDiscount(promotion, subtotal);
        boolean freeShipping = promotion.getType() == PromotionType.FREE_SHIPPING;

        // Increment usage only at real checkout
        promotion.setUsageCount(
                (promotion.getUsageCount() == null ? 0 : promotion.getUsageCount()) + 1
        );
        promotionRepository.save(promotion);

        return PromotionResult.builder()
                .promotion(promotion)
                .discountAmount(discount)
                .freeShipping(freeShipping)
                .build();
    }

    // ─── 7-check validation ───────────────────────────────────────────────────

    /**
     * Runs all 7 promotion validity checks. Throws IllegalStateException on failure.
     */
    private void runAllChecks(Promotion promotion, BigDecimal subtotal, User user) {

        // 1. Cart must not be empty
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Cart is empty.");
        }

        // 2. Promotion must be ACTIVE
        if (promotion.getStatus() != PromotionStatus.ACTIVE) {
            throw new IllegalStateException("Coupon is not active.");
        }

        // 3. Must have started already
        if (promotion.getStartAt() != null && promotion.getStartAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon is not active yet.");
        }

        // 4. Must not be expired
        if (promotion.getExpiryAt() != null && promotion.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon code is expired.");
        }

        // 5. Global usage limit
        if (promotion.getUsageLimit() != null
                && promotion.getUsageCount() != null
                && promotion.getUsageCount() >= promotion.getUsageLimit()) {
            throw new IllegalStateException("Coupon usage limit reached.");
        }

        // 6. Per-user: current user must not have already used this coupon
        if (user != null && orderRepository.existsByUserAndPromotion(user, promotion)) {
            throw new IllegalStateException("You have already used this coupon.");
        }

        // 7. Minimum order amount
        if (promotion.getMinimumOrder() != null
                && subtotal.compareTo(promotion.getMinimumOrder()) < 0) {
            throw new IllegalStateException(
                    "Minimum order amount is $" + promotion.getMinimumOrder().setScale(2, RoundingMode.HALF_UP) + ".");
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private BigDecimal calculateDiscount(Promotion promotion, BigDecimal subtotal) {
        BigDecimal discount = switch (promotion.getType()) {
            case PERCENTAGE -> {
                if (promotion.getValue() == null) yield BigDecimal.ZERO;
                yield subtotal
                        .multiply(promotion.getValue())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            }
            case FIXED_AMOUNT -> promotion.getValue() == null ? BigDecimal.ZERO : promotion.getValue();
            case FREE_SHIPPING -> BigDecimal.ZERO;
        };
        // Discount cannot exceed the subtotal
        return discount.compareTo(subtotal) > 0 ? subtotal : discount;
    }

    private PromotionResult emptyResult() {
        return PromotionResult.builder()
                .promotion(null)
                .discountAmount(BigDecimal.ZERO)
                .freeShipping(false)
                .build();
    }

    private void validateDates(PromotionRequest request) {
        if (request.getStartAt() != null
                && request.getExpiryAt() != null
                && request.getExpiryAt().isBefore(request.getStartAt())) {
            throw new IllegalArgumentException("expiryAt must be after startAt.");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new RuntimeException("Status cannot be blank or null");
        }
    }

    private Promotion findByOrThrow(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion"));
    }
}
