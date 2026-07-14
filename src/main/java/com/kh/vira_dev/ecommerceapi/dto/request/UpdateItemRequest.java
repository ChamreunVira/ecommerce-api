package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateItemRequest {

    @NotNull
    @Min(value = 0 , message = "quantity must be 0 or more")
    private Integer quantity;

}
