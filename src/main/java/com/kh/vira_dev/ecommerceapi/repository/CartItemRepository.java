package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Short> {
}
