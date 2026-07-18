package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.ReviewRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ReviewResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAll() {
        List<ReviewResponse> response = reviewService.findAll();
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getById(@PathVariable Long id) {
        ReviewResponse response = reviewService.findById(id);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> create(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.create(request);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> update(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success(null));
    }

}
