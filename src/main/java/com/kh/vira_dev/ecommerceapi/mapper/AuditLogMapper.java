package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.response.AuditLogResponse;
import com.kh.vira_dev.ecommerceapi.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLogResponse toResponse(AuditLog auditLog) {

            return AuditLogResponse.builder()
                    .id(auditLog.getId())
                    .action(auditLog.getAction())
                    .module(auditLog.getModule())
                    .entityId(auditLog.getEntityId())
                    .performedBy(auditLog.getPerformedBy())
                    .role(auditLog.getRole())
                    .ipAddress(auditLog.getIpAddress())
                    .timestamp(auditLog.getTimestamp())
                    .details(auditLog.getDetails())
                    .reason(auditLog.getReason())
                    .oldValues(auditLog.getOldValues())
                    .newValues(auditLog.getNewValues())
                    .build();
        }

}
