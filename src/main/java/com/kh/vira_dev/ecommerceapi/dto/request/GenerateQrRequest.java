package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateQrRequest {

    @NotNull(message = "Order id is required.")
    private Long orderId;

}
