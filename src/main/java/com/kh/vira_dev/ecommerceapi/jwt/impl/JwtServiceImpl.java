package com.kh.vira_dev.ecommerceapi.jwt.impl;

import com.kh.vira_dev.ecommerceapi.jwt.JwtConfig;
import com.kh.vira_dev.ecommerceapi.jwt.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl extends JwtConfig implements JwtService {

    @Override
    public SecretKey getSigningKey() {
        byte[] keyBytes = getBase64Secret().getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(String subject) {
        return generateToken(subject , new HashMap<>());
    }

    @Override
    public String generateToken(String subject, Map<String, Object> claims) {
        return builderJwtToken(claims , subject , getExpiration());
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = userDetails.getUsername();
        return (userEmail.equals(extractEmail(token)) && !isTokenExpiration(token));
    }

    @Override
    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private boolean isTokenExpiration(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T>T extractClaims(String token, Function<Claims, T> resolve) {
        final Claims claims = extractAllClaims(token);
        return resolve.apply(claims);
    }


    @Override
    public Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException e) {
            log.info("Invalid format for JWT token");
            throw new JwtException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
            throw new JwtException("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
            throw new JwtException("Unsupported JWT token");
        } catch (JwtException e) {
            log.error("Invalid token {}" , e.getLocalizedMessage());
            throw new JwtException("Invalid token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private String builderJwtToken(Map<String , Object> claims , String subject, Long expiration) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
}
