package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.dto.request.BannerRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.BannerResponse;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    /** Public — used by the storefront to display active banners */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getAllActive() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAllActive()));
    }

    /** Admin — all banners regardless of status */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_STAFF')")
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_STAFF')")
    public ResponseEntity<ApiResponse<BannerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Audit(action = AuditAction.CREATE, module = AuditModule.BANNER)
    public ResponseEntity<ApiResponse<BannerResponse>> create(@Valid @ModelAttribute BannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Audit(action = AuditAction.UPDATE, module = AuditModule.BANNER, entityIdParam = "id")
    public ResponseEntity<ApiResponse<BannerResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute BannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.update(id, request)));
    }

    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Audit(action = AuditAction.STATUS_CHANGE, module = AuditModule.BANNER, entityIdParam = "id")
    public ResponseEntity<ApiResponse<BannerResponse>> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.toggleActive(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Audit(action = AuditAction.DELETE, module = AuditModule.BANNER, entityIdParam = "id")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
