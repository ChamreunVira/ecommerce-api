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
public class OrderItemResponse {

    private Long orderItemId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private BigDecimal unitPrice;

    private float discountRate;

    private BigDecimal finalPrice;

    private int quantity;

    private BigDecimal subtotal;

}
