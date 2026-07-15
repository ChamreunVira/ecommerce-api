package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private Long userId;

    private String username;
    
    private String name;

    private String description;

    private double price;

    private float discount;

    private int qty;

    private List<String> images;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
