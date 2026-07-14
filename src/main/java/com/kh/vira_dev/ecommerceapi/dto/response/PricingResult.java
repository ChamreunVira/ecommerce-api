package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PricingResult {

    private BigDecimal subtotal;

    private BigDecimal productDiscount;

    private BigDecimal couponDiscount;

    private BigDecimal sippingFee;

    private BigDecimal totalAmount;

}
