package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserRequest request) {
        User user = new User();
        applyToUserField(user , request);
        return user;
    }

    public UserResponse toResponse(User user) {

        List<String> roles = user.getRoles().stream()
                .map(Role::name)
                .toList();

        return UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .refreshToken(user.getRefreshToken().getToken())
                .accessToken(null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    public void applyToUserField(User user , UserRequest request) {
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(true);
        user.setRoles(request.getRoles().isEmpty() ? Set.of(Role.ROLE_CUSTOMER) : request.getRoles());
    }

}
