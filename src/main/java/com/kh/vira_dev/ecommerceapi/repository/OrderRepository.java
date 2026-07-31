package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.entity.Order;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByUserAndOrderStatus(User user , OrderStatus orderStatus);

    Optional<Order> findByIdAndUser(Long orderId, User user);

    List<Order> findByUser(User user);

    List<Order> findTop5ByOrderByCreatedAtDesc();

    @Query("""
    SELECT
        MONTH(o.createdAt),
        SUM(o.totalAmount),
        COUNT(o.id)
    FROM Order o
    WHERE o.orderStatus = 'PENDING'
    GROUP BY MONTH(o.createdAt)
    """)
    List<Object[]> getRevenueByMonth();

    @Query("""
    SELECT count(o) FROM Order o
    """)
    int countTotalOrder();

    @Query("""
    SELECT coalesce(sum(o.subtotal) , 0) FROM Order o WHERE o.orderStatus = 'PENDING'
    """)
    int getTotalRevenue();
}