package com.kh.vira_dev.ecommerceapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {

    private Long productId;

    private Integer rating;

    private String comment;

}
