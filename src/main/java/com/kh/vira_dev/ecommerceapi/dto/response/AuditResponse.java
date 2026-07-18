package com.kh.vira_dev.ecommerceapi.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.enums.AuditStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditResponse {

    private String logId;

    private AuditAction action;

    private AuditModule module;

    private String entityName;

    private String entityId;

    private String performedBy;

    private String role;

    private String ipAddress;

    private String endpoint;

    private String method;

    private AuditStatus status;

    private String description;

    private String reason;

    private JsonNode oldValues;

    private JsonNode newValues;

    private LocalDateTime timestamp;


}
