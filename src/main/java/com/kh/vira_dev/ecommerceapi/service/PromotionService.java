package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;

import java.math.BigDecimal;

public interface PromotionService {

    PromotionResult applyCoupon(String code, BigDecimal subtotal);

}
