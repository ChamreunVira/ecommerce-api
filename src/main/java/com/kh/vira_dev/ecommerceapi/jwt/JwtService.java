package com.kh.vira_dev.ecommerceapi.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Map;

public interface JwtService {

    SecretKey getSigningKey();

    String generateToken(String subject);

    String generateToken(String subject, Map<String, Object> claims);

    boolean isTokenValid(String token , UserDetails userDetails);

    String extractEmail(String token);

    Claims extractAllClaims(String token);

}
