package com.kh.vira_dev.ecommerceapi.aspect;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.repository.AuditRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Audit auditable) throws Throwable{
        System.out.println("Befor AOP work.");

        System.out.print("Result");
        Object result = pjp.proceed();
        System.out.println(result);

        System.out.println("Signature: " + pjp.getSignature());
        MethodSignature sig = (MethodSignature)pjp.getSignature();
        String[] parameterNames = sig.getParameterNames();
        Object[] args = pjp.getArgs();
        System.out.println("ParameterNames: " + parameterNames);
        System.out.println("Args: " + Arrays.toString(args));

        for(int i = 0; i < parameterNames.length; i++ ) {
            System.out.println("Parameter: " + parameterNames[i]);
            if(parameterNames[i].equals("request")) {
                System.out.println(args[i]);
            }else {
                System.out.println("Unknow");
            }
        }

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        System.out.println("IP: " + ip);
        System.out.println("After AOP work.");

        return pjp.proceed();
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }

    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");

        if(xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

}
