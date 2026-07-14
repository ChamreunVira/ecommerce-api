package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.StoreSettingRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.StoreSettingResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.StoreSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class StoreSettingController {

    private final StoreSettingService storeSettingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreSettingResponse>>> getAll() {
        List<StoreSettingResponse> response = storeSettingService.getAll();
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreSettingResponse>> getOne(@PathVariable Long id) {
        StoreSettingResponse response = storeSettingService.getById(id);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StoreSettingResponse>> save(@RequestBody StoreSettingRequest storeSettingRequest) {
        StoreSettingResponse response = storeSettingService.create(storeSettingRequest);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreSettingResponse>> update(@PathVariable Long id, StoreSettingRequest storeSettingRequest) {
        StoreSettingResponse response = storeSettingService.update(id, storeSettingRequest);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreSettingResponse>> delete(@PathVariable Long id) {
        storeSettingService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success(null));
    }
}
