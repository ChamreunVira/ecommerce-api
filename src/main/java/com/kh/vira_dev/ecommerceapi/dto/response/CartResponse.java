package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

    private Short id;

    private int userId;

    private List<CartItemResponse> cartItems;

    private Integer totalItems;

    private Double totalAmount;

    private Instant updatedAt;

}
