package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "name is required.")
    private String name;

    @NotBlank(message = "description is required.")
    private String description;

}
