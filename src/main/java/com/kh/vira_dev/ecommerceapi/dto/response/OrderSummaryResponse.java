package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import com.kh.vira_dev.ecommerceapi.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSummaryResponse {

    private Long orderId;

    private String orderCode;

    private OrderStatus status;

    private PaymentMethod paymentMethod;

    private BigDecimal totalAmount;

    private int totalItems;

    private String primaryImage;

    private LocalDate createdAt;

}
