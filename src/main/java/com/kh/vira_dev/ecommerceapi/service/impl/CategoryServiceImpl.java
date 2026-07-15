package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.CategoryRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryTrendResponse;
import com.kh.vira_dev.ecommerceapi.entity.Category;
import com.kh.vira_dev.ecommerceapi.exception.DuplicateResourceException;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.CategoryMapper;
import com.kh.vira_dev.ecommerceapi.repository.CategoryRepository;
import com.kh.vira_dev.ecommerceapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {

        if(categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category");
        }
        Category category = categoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        log.info("Created Category: {}", category);
        return categoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findByOrThrow(id);
        categoryMapper.applyToCategoryFields(category, request);

        Category saved = categoryRepository.save(category);
        log.info("Category updated: {}", saved);
        return categoryMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Category category = findByOrThrow(id);
        log.info("Delete category {}", category.getName());
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = findByOrThrow(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toResponseList(categories);
    }

    @Override
    public List<CategoryTrendResponse> getTrend() {
        return categoryRepository.findAllByOrderByRevenueDesc();
    }

    @Override
    public CategoryResponse status(Long id, boolean status) {
        Category category = findByOrThrow(id);
        if(category.getStatus() == status) {
            return categoryMapper.toResponse(category);
        }
        category.setStatus(status);
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    private Category findByOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category"));
    }






















}
