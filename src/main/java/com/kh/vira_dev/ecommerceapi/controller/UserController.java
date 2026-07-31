package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.dto.request.ChangePasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('USER_READ') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        UserResponse userResponse = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAll()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE') or hasAuthority('ROLE_ADMIN')")
    @Audit(action = AuditAction.UPDATE, module = AuditModule.USER, entityIdParam = "id")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable Long id, @Valid @RequestBody UserRequest request){
        UserResponse response = userService.update(id , request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE') or hasAuthority('ROLE_ADMIN')")
    @Audit(action = AuditAction.STATUS_CHANGE, module = AuditModule.USER, entityIdParam = "id")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable Long id) {
        userService.updateStatus(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(Authentication authentication) {
        return ResponseEntity.ok(authentication != null && authentication.isAuthenticated());
    }

    @PutMapping("/change-password")
    @Audit(action = AuditAction.RESET_PASSWORD, module = AuditModule.USER)
    public ResponseEntity<ApiResponse<UserResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        UserResponse response = authService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
