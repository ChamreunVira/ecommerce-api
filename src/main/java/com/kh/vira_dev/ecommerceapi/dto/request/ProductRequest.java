package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotNull(message = "category id is required.")
    private Long categoryId;

    @NotBlank(message = "name is required.")
    private String name;

    @NotBlank(message = "description is required.")
    private String description;

    @NotNull(message = "price is required.")
    private double price;

    @NotNull(message = "discount is required.")
    private float discount;

    @NotNull(message = "qty is required.")
    private int qty;

    private List<MultipartFile> images;

}
