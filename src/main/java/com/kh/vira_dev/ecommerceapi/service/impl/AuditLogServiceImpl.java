package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.response.AuditLogResponse;
import com.kh.vira_dev.ecommerceapi.entity.AuditLog;
import com.kh.vira_dev.ecommerceapi.mapper.AuditLogMapper;
import com.kh.vira_dev.ecommerceapi.repository.AuditRepository;
import com.kh.vira_dev.ecommerceapi.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditRepository auditRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public Page<AuditLogResponse> getPage(Pageable pageable) {
        Page<AuditLog> auditLog = auditRepository.findAll(pageable);
        return auditLog.map(auditLogMapper::toResponse);
    }

}
