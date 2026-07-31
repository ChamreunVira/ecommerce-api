package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.entity.Permission;
import com.kh.vira_dev.ecommerceapi.entity.Role;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User toEntity(UserRequest request) {
        User user = new User();
        applyToUserField(user, request);
        return user;
    }

    public UserResponse toResponse(User user) {
        List<String> roles = user.getRoles() != null
                ? user.getRoles().stream().map(Role::getName).toList()
                : Collections.emptyList();

        List<String> permissions = user.getRoles() != null
                ? user.getRoles().stream()
                    .filter(r -> r.getPermissions() != null)
                    .flatMap(r -> r.getPermissions().stream())
                    .map(Permission::getName)
                    .distinct()
                    .toList()
                : Collections.emptyList();

        String refreshTokenStr = user.getRefreshToken() != null ? user.getRefreshToken().getToken() : null;

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .permissions(permissions)
                .refreshToken(refreshTokenStr)
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

    public void applyToUserField(User user, UserRequest request) {
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setStatus(true);

        Set<Role> assignedRoles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                roleRepository.findByName(roleName).ifPresent(assignedRoles::add);
            }
        }
        if (assignedRoles.isEmpty()) {
            roleRepository.findByName("ROLE_CUSTOMER").ifPresent(assignedRoles::add);
        }
        user.setRoles(assignedRoles);
    }
}
