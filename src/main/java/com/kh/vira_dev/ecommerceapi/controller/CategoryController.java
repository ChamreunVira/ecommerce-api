package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.CategoryRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.CategoryTrendResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        var response = categoryService.getAll();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable short id) {
        var response = categoryService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/trend")
    public ResponseEntity<ApiResponse<List<CategoryTrendResponse>>> getTrend() {
        var response = categoryService.getTrend();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        var response = categoryService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable short id, @Valid @RequestBody CategoryRequest request) {
        var response = categoryService.update(id,request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateStatus(@PathVariable short id, @RequestParam boolean status) {
        var response = categoryService.status(id , status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable short id) {
        categoryService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success("Deleted category successfully!"));
    }
}
