package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentResponse {

    private Long id;

    private String code;

    private String orderCode;

    private String customer;

    private ShipmentStatus status;

    private String destination;

    private String trackingNumber;

    private LocalDateTime estimatedDelivery;

    private String carrier;

}