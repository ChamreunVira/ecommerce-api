package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;

    private CustomerInfo customer;

    private ProductInfo product;

    private Integer rating;

    private String comment;

    private String status;

    private LocalDateTime createdAt;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CustomerInfo {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductInfo {
        private Long id;
        private String name;
    }
}
