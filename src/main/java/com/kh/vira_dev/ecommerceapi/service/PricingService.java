package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.response.PricingResult;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;
import com.kh.vira_dev.ecommerceapi.entity.Cart;
import com.kh.vira_dev.ecommerceapi.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PricingService {

    private static final BigDecimal FLAT_SHIPPING_FEE = new BigDecimal("0.00");

    private final PromotionService promotionService;

    public PricingResult calculate(Cart cart, String couponCode) {

        BigDecimal subtotal = calculateSubtotal(cart);

        BigDecimal productDiscount = calculateProductDiscount(cart);
        PromotionResult promotion = promotionService.applyCoupon(couponCode, subtotal);

        BigDecimal shippingFee = promotion.isFreeShipping()
                ? BigDecimal.ZERO
                : FLAT_SHIPPING_FEE;

        BigDecimal totalAmount = subtotal
                .subtract(productDiscount)
                .subtract(promotion.getDiscountAmount())
                .add(shippingFee);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        return PricingResult.builder()
                .subtotal(subtotal.setScale(2, RoundingMode.HALF_UP))
                .productDiscount(productDiscount.setScale(2, RoundingMode.HALF_UP))
                .shippingFee(shippingFee.setScale(2, RoundingMode.HALF_UP))
                .couponDiscount(promotion.getDiscountAmount().setScale(2, RoundingMode.HALF_UP))
                .totalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP))
                .promotion(promotion.getPromotion())
                .build();
    }

    private BigDecimal calculateSubtotal(Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateProductDiscount(Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(this::calculateItemDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemDiscount(CartItem item) {
        if (item.getProduct().getPrice() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = BigDecimal.valueOf(item.getProduct().getPrice());
        float discount = item.getProduct().getDiscountRate() / 100;
        BigDecimal discountPerUnit = price.multiply(BigDecimal.valueOf(discount));

        return discountPerUnit.multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
