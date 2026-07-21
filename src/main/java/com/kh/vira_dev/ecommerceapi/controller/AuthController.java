package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.dto.request.AuthRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.ResetPasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.SendOtpRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.OtpResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.RefreshTokenResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.VerifyOtpResponse;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.RefreshTokenService;
import com.kh.vira_dev.ecommerceapi.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<UserResponse>> signUp(@Valid @RequestBody UserRequest request){
        UserResponse response = userService.create(request);

        ResponseCookie cookie = ResponseCookie
                .from("refresh_token" , response.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(365))
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE , cookie.toString())
                .body(ApiResponse.success(response));
    }

    @PostMapping("/sign-in")
    @Audit(action = AuditAction.LOGIN , module = AuditModule.USER)
    public ResponseEntity<ApiResponse<UserResponse>> signIn(@Valid @RequestBody AuthRequest request) {
        var response = authService.singIn(request);
        ResponseCookie cookie = ResponseCookie
                .from("refresh_token" , response.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(365))
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE , cookie.toString()).body(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> newAccessToken(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh_token")) {
                refresh = cookie.getValue();
                break;
            }
        }
        RefreshTokenResponse response = refreshTokenService.verify(refresh);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        var response = authService.me();
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtp(@RequestBody Map<String , String> request) {
        VerifyOtpResponse response = authService.verifyOtp(request.get("email") , request.get("otp"));
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<OtpResponse>> sendResetOtp(@RequestBody SendOtpRequest request) {
        OtpResponse response = authService.sendResetOtp(request);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<UserResponse>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        UserResponse response = authService.resetPassword(request);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
