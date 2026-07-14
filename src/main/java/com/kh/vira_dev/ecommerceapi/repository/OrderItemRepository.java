package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Short> {

}
