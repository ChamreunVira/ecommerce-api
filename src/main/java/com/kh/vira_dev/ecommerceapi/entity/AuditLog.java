package com.kh.vira_dev.ecommerceapi.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.enums.AuditStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_audit_log")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logId;

    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    private AuditModule module;

    private String entityName;

    private String entityId;

    private Long performedById;

    private String performedBy;

    private String role;

    private String ipAddress;

    private String userAgent;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    private String endpoint;

    private String httpMethod;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode oldValues;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode newValues;

    private LocalDateTime createdAt;

}
