package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
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
public class RecentOrderResponse {

    private Long orderId;

    private String orderCode;

    private String fullName;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private LocalDate createdDate;
}
