package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.RoleRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.PermissionResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.RoleResponse;
import com.kh.vira_dev.ecommerceapi.entity.Permission;
import com.kh.vira_dev.ecommerceapi.entity.Role;
import com.kh.vira_dev.ecommerceapi.repository.PermissionRepository;
import com.kh.vira_dev.ecommerceapi.repository.RoleRepository;
import com.kh.vira_dev.ecommerceapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return mapToResponse(role);
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        String roleName = request.getName().trim();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName.toUpperCase();
        }

        if (roleRepository.existsByName(roleName)) {
            throw new RuntimeException("Role with name " + roleName + " already exists");
        }

        Set<Permission> permissions = new HashSet<>();
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            permissions.addAll(permissionRepository.findAllById(request.getPermissionIds()));
        }

        Role role = Role.builder()
                .name(roleName)
                .description(request.getDescription())
                .permissions(permissions)
                .build();

        Role savedRole = roleRepository.save(role);
        return mapToResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!newName.startsWith("ROLE_")) {
                newName = "ROLE_" + newName.toUpperCase();
            }
            if (!role.getName().equalsIgnoreCase(newName) && roleRepository.existsByName(newName)) {
                throw new RuntimeException("Role with name " + newName + " already exists");
            }
            role.setName(newName);
        }

        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }

        if (request.getPermissionIds() != null) {
            Set<Permission> newPermissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));
            role.setPermissions(newPermissions);
        }

        Role updatedRole = roleRepository.save(role);
        return mapToResponse(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if ("ROLE_ADMIN".equalsIgnoreCase(role.getName())) {
            throw new RuntimeException("Cannot delete default ADMIN role");
        }

        roleRepository.delete(role);
    }

    private RoleResponse mapToResponse(Role role) {
        List<PermissionResponse> permissionResponses = role.getPermissions() != null
                ? role.getPermissions().stream()
                    .map(p -> PermissionResponse.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .description(p.getDescription())
                            .module(p.getModule())
                            .build())
                    .toList()
                : List.of();

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(permissionResponses)
                .build();
    }
}
