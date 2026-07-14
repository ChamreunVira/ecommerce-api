package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.ChangePasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.ResetPasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.SendOtpRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.OtpResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable int id) {
        UserResponse userResponse = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable int id, @Valid @RequestBody UserRequest request){
        UserResponse response = userService.update(id , request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable int id) {
        userService.updateStatus(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(Authentication authentication) {
        return ResponseEntity.ok(authentication.isAuthenticated());
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<UserResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        UserResponse response = authService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
