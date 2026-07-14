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
public class CategoryResponse {

    private short id;

    private String name;

    private String description;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private List<ProductResponse> products;

    private Boolean status;

}
