package com.kh.vira_dev.ecommerceapi.annotation;

import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    AuditAction action();
    AuditModule module();
    String entityIdParam() default "id";

}
