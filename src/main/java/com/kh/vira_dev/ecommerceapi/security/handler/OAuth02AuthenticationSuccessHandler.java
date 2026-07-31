package com.kh.vira_dev.ecommerceapi.security.handler;

import com.kh.vira_dev.ecommerceapi.entity.RefreshToken;
import com.kh.vira_dev.ecommerceapi.entity.Role;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.jwt.JwtService;
import com.kh.vira_dev.ecommerceapi.repository.RoleRepository;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth02AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${refresh-token.expiry-date}")
    private Long expiryDate;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @NullMarked
    @org.springframework.transaction.annotation.Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        if (oAuth2User == null) {
            throw new RuntimeException("Something went wrong");
        }
        String fullName = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        String token = jwtService.generateToken(email);

        User existsUser = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setFullName(fullName != null ? fullName : email);
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

                    Set<Role> roles = new HashSet<>();
                    roleRepository.findByName("ROLE_CUSTOMER").ifPresent(roles::add);
                    user.setRoles(roles);

                    user.setStatus(true);
                    User savedUser = userRepository.save(user);
                    RefreshToken refreshToken = refreshTokenService.refresh(savedUser);
                    savedUser.setRefreshToken(refreshToken);
                    return savedUser;
                });

        if (existsUser.getRefreshToken() == null) {
            RefreshToken refreshToken = refreshTokenService.refresh(existsUser);
            existsUser.setRefreshToken(refreshToken);
        }

        Cookie cookie = new Cookie("token", existsUser.getRefreshToken().getToken());

        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000?token=" + token);
    }
}