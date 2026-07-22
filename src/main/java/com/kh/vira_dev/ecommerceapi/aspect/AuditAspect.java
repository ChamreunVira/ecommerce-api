package com.kh.vira_dev.ecommerceapi.aspect;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.entity.AuditLog;
import com.kh.vira_dev.ecommerceapi.repository.AuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "password", "accessToken", "refreshToken"
    );

    private static final String REDACTED = "***REDACTED***";

    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(audit)")
    public Object auditLog(ProceedingJoinPoint pjp, Audit audit) throws Throwable {
        String entityId = resolveEntityId(pjp, audit.entityIdParam());
        Object oldValue = tryCaptureOldValue(pjp, entityId);

        AuditLog ad = new AuditLog();
        ad.setEntityId(entityId);
        ad.setAction(audit.action());
        ad.setModule(audit.module());

        populateActor(ad);
        populateIp(ad);

        try {
            Object result = pjp.proceed();

            ad.setNewValues(safeWriteJson(result));
            ad.setOldValues(safeWriteJson(oldValue));
            ad.setDetails("Success");
            ad.setReason("");
            auditRepository.save(ad);

            return result;
        } catch (Throwable ex) {
            ad.setDetails("Failure");
            ad.setReason(ex.getLocalizedMessage());
            ad.setOldValues(safeWriteJson(oldValue));
            auditRepository.save(ad);
            throw ex;
        }
    }

    private String resolveEntityId(ProceedingJoinPoint pjp, String parameterName) {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String[] parameterNames = sig.getParameterNames();
        Object[] args = pjp.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(parameterName)) {
                return String.valueOf(args[i]);
            }
        }

        return "Unknown";
    }

    private void populateActor(AuditLog auditLog) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            auditLog.setPerformedBy(auth.getName());
            auditLog.setRole(auth.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse("Unknown"));
        } else {
            auditLog.setPerformedBy("System");
            auditLog.setRole("System");
        }
    }

    private void populateIp(AuditLog audit) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String ip = request.getHeader("x-forwarded-for");
            audit.setIpAddress(ip != null ? ip : request.getRemoteAddr());
        } else {
            audit.setIpAddress("N/A");
        }
    }

    private Object tryCaptureOldValue(ProceedingJoinPoint pjp, String parameterName) {
        return null;
    }

    /**
     * Serializes the audit payload safely:
     *  - Unwraps ResponseEntity down to just { body, status } (drops the noisy HttpHeaders object)
     *  - Recursively redacts any field name found in SENSITIVE_FIELDS, at any nesting depth
     */
    private String safeWriteJson(Object result) {
        if (result == null) return null;
        try {
            Object toSerialize = unwrap(result);
            JsonNode tree = objectMapper.valueToTree(toSerialize);
            redactSensitive(tree);
            return objectMapper.writeValueAsString(tree);
        } catch (Exception e) {
            return String.valueOf(result);
        }
    }

    /**
     * Pulls the meaningful payload out of framework wrapper types instead of
     * serializing them as-is (ResponseEntity exposes HttpHeaders, which Jackson
     * expands into a huge, mostly-null nested structure).
     */
    private Object unwrap(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            return Map.of(
                    "status", responseEntity.getStatusCode().value(),
                    "body", responseEntity.getBody() != null ? responseEntity.getBody() : Map.of()
            );
        }
        return result;
    }

    /**
     * Walks the JSON tree in place, replacing the value of any object field
     * whose name is in SENSITIVE_FIELDS with a redacted placeholder — no matter
     * how deeply nested it is (e.g. body.data.accessToken).
     */
    private void redactSensitive(JsonNode node) {
        if (node == null) return;

        if (node.isObject()) {
            ObjectNode obj = (ObjectNode) node;
            for (Map.Entry<String, JsonNode> entry : obj.properties()) {
                String fieldName = entry.getKey();
                JsonNode value = entry.getValue();

                if (SENSITIVE_FIELDS.contains(fieldName)) {
                    obj.put(fieldName, REDACTED);
                } else {
                    redactSensitive(value);
                }
            }
        } else if (node.isArray()) {
            ArrayNode arr = (ArrayNode) node;
            for (JsonNode item : arr) {
                redactSensitive(item);
            }
        }
    }
}