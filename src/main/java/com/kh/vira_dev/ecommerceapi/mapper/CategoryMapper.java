package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.CategoryRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.ProductResponse;
import com.kh.vira_dev.ecommerceapi.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ProductMapper productMapper;

    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        applyToCategoryFields(category, request);
        return category;
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse toResponse(Category category) {


        List<ProductResponse> existProducts = new ArrayList<>();
        if(category.getProducts() != null) {
            existProducts.addAll(productMapper.toResponseList(category.getProducts()));
        }

        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .products(existProducts)
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public void applyToCategoryFields(Category category , CategoryRequest request) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
    }

}
