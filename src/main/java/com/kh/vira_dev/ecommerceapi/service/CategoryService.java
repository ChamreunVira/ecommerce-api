package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.CategoryRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryTrendResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    List<CategoryTrendResponse> getTrend();

    CategoryResponse status(Long id, boolean status);
}
