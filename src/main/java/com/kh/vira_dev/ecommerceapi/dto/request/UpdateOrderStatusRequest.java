package com.kh.vira_dev.ecommerceapi.dto.request;

import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {

    @NotNull(message = "status is required.")
    private OrderStatus status;

    @Size(max = 100)
    private String trackingNumber;

}
