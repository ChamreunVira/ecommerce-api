package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.CategoryRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryTrendResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(short id, CategoryRequest request);

    void delete(short id);

    CategoryResponse getById(short id);

    List<CategoryResponse> getAll();

    List<CategoryTrendResponse> getTrend();

    CategoryResponse status(short id, boolean status);
}
