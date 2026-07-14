package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusUpdateResponse {

    private short orderId;

    private String orderCode;

    private OrderStatus status;

    private String trackingNumber;

    private LocalDate updatedAt;

}
