package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.PromotionRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResponse;
import com.kh.vira_dev.ecommerceapi.entity.Promotion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionMapper {

    public Promotion toEntity(PromotionRequest request) {
        Promotion promotion = new Promotion();
        applyPromotionFields(promotion, request);
        promotion.setUsageCount(0);
        return promotion;
    }

    public PromotionResponse toResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .id(promotion.getId())
                .code(promotion.getCode())
                .type(promotion.getType())
                .value(promotion.getValue())
                .minimumOrder(promotion.getMinimumOrder())
                .usageCount(promotion.getUsageCount())
                .usageLimit(promotion.getUsageLimit())
                .status(promotion.getStatus())
                .startAt(promotion.getStartAt())
                .expiryAt(promotion.getExpiryAt())
                .build();
    }

    public List<PromotionResponse> toResponseList(List<Promotion> promotions) {
        return promotions.stream()
                .map(this::toResponse)
                .toList();
    }

    public void applyPromotionFields(Promotion promotion, PromotionRequest request) {
        promotion.setCode(request.getCode().trim().toUpperCase());
        promotion.setType(request.getType());
        promotion.setValue(request.getValue());
        promotion.setMinimumOrder(request.getMinimumOrder());
        promotion.setUsageLimit(request.getUsageLimit());
        promotion.setStatus(request.getStatus());
        promotion.setStartAt(request.getStartAt());
        promotion.setExpiryAt(request.getExpiryAt());
    }
}
