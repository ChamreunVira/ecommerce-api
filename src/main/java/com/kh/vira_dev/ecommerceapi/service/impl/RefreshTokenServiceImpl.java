package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.response.RefreshTokenResponse;
import com.kh.vira_dev.ecommerceapi.entity.RefreshToken;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.jwt.JwtService;
import com.kh.vira_dev.ecommerceapi.repository.RefreshTokenRepository;
import com.kh.vira_dev.ecommerceapi.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${refresh-token.expiry-date}")
    private Long expiryDate;

    @Override
    public RefreshToken refresh(User user) {
        String token = jwtService.generateToken(user.getFullName());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);

        Instant expiry = LocalDateTime.now()
                        .plusMonths(expiryDate)
                                .atZone(ZoneId.systemDefault())
                                        .toInstant();
        refreshToken.setExpiryDate(expiry);
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshTokenResponse verify(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh"));
        
        if(!refreshToken.getExpiryDate().isBefore(Instant.now())) {
            String accessToken = jwtService.generateToken(refreshToken.getUser().getEmail());
            String refresh = jwtService.generateToken(refreshToken.getUser().getEmail());
            refreshToken.setToken(refresh);
            return toResponse(accessToken);
        }

       throw new IllegalArgumentException("Refresh token expired");
    }

    private RefreshTokenResponse toResponse(String accessToken) {
        return RefreshTokenResponse
                .builder()
                .accessToken(accessToken)
                .build();
    }
}
