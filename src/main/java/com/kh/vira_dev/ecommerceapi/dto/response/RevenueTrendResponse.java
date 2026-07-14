package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueTrendResponse {

    private int month;

    private BigDecimal revenue;

    private int order;

}
