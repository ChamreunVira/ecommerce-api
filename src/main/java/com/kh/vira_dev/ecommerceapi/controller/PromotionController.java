package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.dto.request.PromotionRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResponse;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @Audit(action = AuditAction.CREATE, module = AuditModule.PROMOTION)
    public ResponseEntity<ApiResponse<PromotionResponse>> create(@Valid @RequestBody PromotionRequest request) {
        PromotionResponse response = promotionService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Audit(action = AuditAction.UPDATE, module = AuditModule.PROMOTION, entityIdParam = "id")
    public ResponseEntity<ApiResponse<PromotionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequest request
    ) {
        PromotionResponse response = promotionService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

   @PutMapping("/{id}/status")
    @Audit(action = AuditAction.STATUS_CHANGE, module = AuditModule.PROMOTION, entityIdParam = "id")
    public ResponseEntity<ApiResponse<PromotionResponse>> updateStatus(@PathVariable Long id, @RequestBody Map<String , String> body) {
        PromotionResponse response = promotionService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok(ApiResponse.success(response));
   }

}
