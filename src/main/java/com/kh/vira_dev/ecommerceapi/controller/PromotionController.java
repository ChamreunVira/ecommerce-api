package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.PromotionRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PromotionResponse>>> getAll() {
        List<PromotionResponse> responses = promotionService.getAll();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PromotionResponse>> getById(@PathVariable Long id) {
        PromotionResponse response = promotionService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PromotionResponse>> create(@Valid @RequestBody PromotionRequest request) {
        PromotionResponse response = promotionService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PromotionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequest request
    ) {
        PromotionResponse response = promotionService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        promotionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted promotion successfully!"));
    }

}
