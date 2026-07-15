package com.kh.vira_dev.ecommerceapi.dto.request;

import com.kh.vira_dev.ecommerceapi.enums.ShipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentRequest {

    private Long orderId;

    @NotNull(message = "Status is required.")
    private ShipmentStatus status;

    @NotBlank(message = "Destination is required.")
    private String destination;

    @NotBlank(message = "trackingNumber is required.")
    private String trackingNumber;

    private LocalDateTime estimatedDelivery;

    private String carrier;

}
