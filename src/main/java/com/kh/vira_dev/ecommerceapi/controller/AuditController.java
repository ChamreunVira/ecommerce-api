package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.PageRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.AuditLogResponse;
import com.kh.vira_dev.ecommerceapi.payload.PageResponse;
import com.kh.vira_dev.ecommerceapi.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<PageResponse<AuditLogResponse>> getPage(PageRequest request) {
        Page<AuditLogResponse> response = auditLogService.getPage(request.toPageable());
        return ResponseEntity.ok(PageResponse.success("Success to retrieve audit-log page." , response));
    }

}
