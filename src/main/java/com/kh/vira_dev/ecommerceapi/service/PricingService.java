package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.response.PricingResult;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;
import com.kh.vira_dev.ecommerceapi.entity.Cart;
import com.kh.vira_dev.ecommerceapi.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final PromotionService promotionService;

    public PricingResult calculate(Cart cart, String couponCode) {

        BigDecimal subtotal = calculateSubtotal(cart);

        BigDecimal productDiscount = calculateProductDiscount(cart);
        PromotionResult pricing = promotionService.applyCoupon(couponCode, subtotal);

        BigDecimal totalAmount = subtotal
                .subtract(productDiscount)
                .subtract(pricing.getDiscountAmount())
                .add(BigDecimal.ZERO);

        return PricingResult.builder()
                .subtotal(subtotal)
                .productDiscount(productDiscount)
                .sippingFee(BigDecimal.ZERO)
                .couponDiscount(pricing.getDiscountAmount())
                .totalAmount(totalAmount)
                .build();
    }

    private BigDecimal calculateSubtotal(Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO , BigDecimal::add);
    }

    private BigDecimal calculateProductDiscount(Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(this::calculateItemDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemDiscount(CartItem item) {
        if(item.getProduct().getPrice() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = BigDecimal.valueOf(item.getProduct().getPrice());
        float discount = item.getProduct().getDiscountRate() / 100;
        BigDecimal discountPerUnit = price.multiply(BigDecimal.valueOf(discount));

        return discountPerUnit.multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}