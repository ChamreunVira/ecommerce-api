package com.kh.vira_dev.ecommerceapi.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Role {
    ROLE_ADMIN(
            Set.of(
                    Permission.USER_READ,
                    Permission.USER_WRITE,
                    Permission.USER_UPDATE,
                    Permission.USER_DELETE
            )
    ),
    ROLE_CUSTOMER(
            Set.of(
                    Permission.PRODUCT_READ,
                    
                    Permission.USER_READ,
                    
                    Permission.CART_READ,
                    Permission.CART_UPDATE,
                    Permission.CART_DELETE
            )
    ),
    ROLE_SELLER(
            Set.of(
                    Permission.USER_READ,
                    
                    Permission.PRODUCT_READ,
                    Permission.PRODUCT_WRITE,
                    
                    Permission.PRODUCT_UPDATE,
                    Permission.PRODUCT_DELETE,
                    
                    Permission.CATEGORY_READ,
                    Permission.CATEGORY_WRITE,
                    Permission.CATEGORY_UPDATE,
                    
                    Permission.ORDER_READ
            )
    );

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
