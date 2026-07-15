package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemRequest {

    @NotNull(message = "product id is required.")
    private Long productId;

    @NotNull
    @Min(value = 1 , message = "quantity must be at least 1")
    private Integer quantity;
}
