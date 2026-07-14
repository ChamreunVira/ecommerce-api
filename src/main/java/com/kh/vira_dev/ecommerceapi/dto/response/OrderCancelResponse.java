package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCancelResponse {

    private Short orderId;

    private String orderCode;

    private OrderStatus status;

    private Instant cancelledAt;

}
