package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopProductResponse {

    private String name;

    private Double sales;

    private BigDecimal revenue;

    public TopProductResponse(String name, Double sales, BigDecimal revenue) {
        this.name = name;
        this.sales = sales;
        this.revenue = revenue;
    }

}
