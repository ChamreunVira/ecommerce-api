package com.kh.vira_dev.ecommerceapi.config;

import com.kh.vira_dev.ecommerceapi.entity.Permission;
import com.kh.vira_dev.ecommerceapi.entity.Role;
import com.kh.vira_dev.ecommerceapi.repository.PermissionRepository;
import com.kh.vira_dev.ecommerceapi.repository.RoleRepository;
import com.kh.vira_dev.ecommerceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        log.info("Checking dynamic RBAC Permissions and Roles initialization...");

        // 1. Seed Permissions
        Map<String, String[]> permissionsByModule = new LinkedHashMap<>();
        permissionsByModule.put("USER", new String[]{"USER_READ", "USER_WRITE", "USER_UPDATE", "USER_DELETE"});
        permissionsByModule.put("CATEGORY", new String[]{"CATEGORY_READ", "CATEGORY_WRITE", "CATEGORY_UPDATE", "CATEGORY_DELETE"});
        permissionsByModule.put("PRODUCT", new String[]{"PRODUCT_READ", "PRODUCT_WRITE", "PRODUCT_UPDATE", "PRODUCT_DELETE"});
        permissionsByModule.put("ORDER", new String[]{"ORDER_READ", "ORDER_WRITE", "ORDER_UPDATE", "ORDER_CANCEL"});
        permissionsByModule.put("CART", new String[]{"CART_READ", "CART_WRITE", "CART_UPDATE", "CART_DELETE"});
        permissionsByModule.put("INVENTORY", new String[]{"INVENTORY_READ", "INVENTORY_UPDATE"});
        permissionsByModule.put("PAYMENT", new String[]{"PAYMENT_READ", "PAYMENT_UPDATE"});
        permissionsByModule.put("SHIPPING", new String[]{"SHIPPING_READ", "SHIPPING_UPDATE"});
        permissionsByModule.put("PROMOTION", new String[]{"PROMOTION_READ", "PROMOTION_WRITE", "PROMOTION_DELETE"});
        permissionsByModule.put("REPORT", new String[]{"REPORT_READ"});
        permissionsByModule.put("REVIEW", new String[]{"REVIEW_READ", "REVIEW_WRITE", "REVIEW_DELETE"});
        permissionsByModule.put("AUDIT", new String[]{"AUDIT_READ"});
        permissionsByModule.put("ROLE", new String[]{"ROLE_READ", "ROLE_WRITE", "ROLE_UPDATE", "ROLE_DELETE"});

        Map<String, Permission> allPermissionsMap = new HashMap<>();

        permissionsByModule.forEach((module, perms) -> {
            for (String permName : perms) {
                Permission perm = permissionRepository.findByName(permName)
                        .orElseGet(() -> permissionRepository.save(
                                Permission.builder()
                                        .name(permName)
                                        .module(module)
                                        .description("Permission for " + permName.toLowerCase().replace('_', ' '))
                                        .build()
                        ));
                allPermissionsMap.put(permName, perm);
            }
        });

        // 2. Seed Default Roles
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .description("Administrator with full system access")
                                .permissions(new HashSet<>(allPermissionsMap.values()))
                                .build()
                ));

        // Ensure ADMIN role always retains newly added permissions if updated
        if (adminRole.getPermissions() == null || adminRole.getPermissions().size() < allPermissionsMap.size()) {
            adminRole.setPermissions(new HashSet<>(allPermissionsMap.values()));
            adminRole = roleRepository.save(adminRole);
        }

        roleRepository.findByName("ROLE_SELLER")
                .orElseGet(() -> {
                    Set<Permission> sellerPerms = new HashSet<>();
                    List.of("PRODUCT_READ", "PRODUCT_WRITE", "PRODUCT_UPDATE", "PRODUCT_DELETE",
                            "CATEGORY_READ", "CATEGORY_WRITE", "CATEGORY_UPDATE",
                            "INVENTORY_READ", "INVENTORY_UPDATE",
                            "ORDER_READ", "ORDER_UPDATE", "REPORT_READ", "USER_READ")
                            .forEach(p -> { if(allPermissionsMap.containsKey(p)) sellerPerms.add(allPermissionsMap.get(p)); });
                    return roleRepository.save(
                            Role.builder()
                                    .name("ROLE_SELLER")
                                    .description("Seller manage products, inventory and order fulfillment")
                                    .permissions(sellerPerms)
                                    .build()
                    );
                });

        roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> {
                    Set<Permission> customerPerms = new HashSet<>();
                    List.of("PRODUCT_READ", "CATEGORY_READ", "USER_READ",
                            "CART_READ", "CART_WRITE", "CART_UPDATE", "CART_DELETE",
                            "ORDER_READ", "ORDER_WRITE", "ORDER_CANCEL", "REVIEW_READ", "REVIEW_WRITE")
                            .forEach(p -> { if(allPermissionsMap.containsKey(p)) customerPerms.add(allPermissionsMap.get(p)); });
                    return roleRepository.save(
                            Role.builder()
                                    .name("ROLE_CUSTOMER")
                                    .description("Default customer account")
                                    .permissions(customerPerms)
                                    .build()
                    );
                });

        // 3. Ensure initial Admin User has ROLE_ADMIN
        Role finalAdminRole = adminRole;
        userRepository.findByEmail("admin@gmail.com").ifPresent(user -> {
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.setRoles(Set.of(finalAdminRole));
                userRepository.save(user);
            }
        });

        log.info("RBAC Permissions and Roles initialization complete.");
    }
}
