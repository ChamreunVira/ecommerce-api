package com.kh.vira_dev.ecommerceapi.security.handler;

import com.kh.vira_dev.ecommerceapi.entity.RefreshToken;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.enums.Role;
import com.kh.vira_dev.ecommerceapi.jwt.JwtService;
import com.kh.vira_dev.ecommerceapi.repository.RefreshTokenRepository;
import com.kh.vira_dev.ecommerceapi.repository.UserRepository;
import com.kh.vira_dev.ecommerceapi.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;   
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth02AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${refresh-token.expiry-date}")
    private Long expiryDate;

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @NullMarked
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        if(oAuth2User == null) {
            throw new RuntimeException("Something went wrong");
        };
        String fullName = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        String token = jwtService.generateToken(oAuth2User.getAttribute("email"));

        User existsUser = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setFullName(fullName);
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setRoles(Set.of(Role.ROLE_CUSTOMER));
                    user.setStatus(true);
                    refreshTokenService.refresh(user);
                    return user;
                });

        Cookie cookie = new Cookie("token" , existsUser.getRefreshToken().getToken());

        // send to front end
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000?token=" + token);
    }
}