package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionResult {

    private Promotion promotion;

    private BigDecimal discountAmount;

}
