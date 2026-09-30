package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.dto.request.PromotionRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PromotionResult;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PromotionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(promotionService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PromotionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(promotionService.getById(id)));
    }

    @PostMapping
    @Audit(action = AuditAction.CREATE, module = AuditModule.PROMOTION)
    public ResponseEntity<ApiResponse<PromotionResponse>> create(@Valid @RequestBody PromotionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(promotionService.create(request)));
    }

    @PutMapping("/{id}")
    @Audit(action = AuditAction.UPDATE, module = AuditModule.PROMOTION, entityIdParam = "id")
    public ResponseEntity<ApiResponse<PromotionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(promotionService.update(id, request)));
    }

    @PutMapping("/{id}/status")
    @Audit(action = AuditAction.STATUS_CHANGE, module = AuditModule.PROMOTION, entityIdParam = "id")
    public ResponseEntity<ApiResponse<PromotionResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return ResponseEntity.ok(ApiResponse.success(promotionService.updateStatus(id, body.get("status"))));
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<PromotionResult>> validate(@RequestBody Map<String, Object> body) {
        String code = (String) body.get("code");
        BigDecimal subtotal = new BigDecimal(body.get("subtotal").toString());
        PromotionResult result = promotionService.validateCoupon(code, subtotal);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
