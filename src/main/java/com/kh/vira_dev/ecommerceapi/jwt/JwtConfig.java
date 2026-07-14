package com.kh.vira_dev.ecommerceapi.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtConfig {

    @Value("${jwt.base64Secret}")
    private String base64Secret;

    @Value("${jwt.expiration}")
    private long expiration;

}
