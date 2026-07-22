package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogResponse {

    private Long id;

    private AuditAction action;

    private AuditModule module;

    private String entityId;

    private String performedBy;

    private String role;

    private String ipAddress;

    private Instant timestamp;

    private String details;

    private String reason;

    private String oldValues;

    private String newValues;
}