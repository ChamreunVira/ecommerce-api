package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.dto.response.TopProductResponse;
import com.kh.vira_dev.ecommerceapi.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
    SELECT new com.kh.vira_dev.ecommerceapi.dto.response.TopProductResponse(
        p.name,
        SUM(p.price * oi.quantity),
        SUM(oi.quantity * oi.unitPrice)
    )
    FROM OrderItem oi
    JOIN oi.product p
    GROUP BY p.name
    ORDER BY SUM(p.price * oi.quantity) DESC
    """)
    List<TopProductResponse> findTop5Product();

}
