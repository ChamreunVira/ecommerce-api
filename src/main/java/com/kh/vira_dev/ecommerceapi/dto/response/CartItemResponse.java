package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal unitPrice;

    private float discountRate;

    private BigDecimal finalPrice;

    private int quantity;

    private BigDecimal subtotal;

}
