package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.PromotionRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {

    PromotionResponse create(PromotionRequest request);

    PromotionResponse update(Long id, PromotionRequest request);

    void delete(Long id);

    PromotionResponse getById(Long id);

    List<PromotionResponse> getAll();

    PromotionResult applyCoupon(String code, BigDecimal subtotal);

}
