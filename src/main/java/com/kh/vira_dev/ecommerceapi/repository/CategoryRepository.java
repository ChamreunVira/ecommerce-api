package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.dto.response.CategoryTrendResponse;
import com.kh.vira_dev.ecommerceapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Short> {

    boolean existsByName(String name);

    @Query(value = """
    SELECT
        c.id,
        c.name,
        SUM(oi.quantity * oi.unit_price) AS revenue
    FROM tbl_category c
         JOIN tbl_product p ON p.category_id = c.id
         JOIN tbl_order_item oi ON oi.product_id = p.id
         JOIN tbl_order o ON o.id = oi.order_id
    WHERE o.order_status = 'PENDING'
    GROUP BY c.id, c.name
    ORDER BY revenue DESC
    """ , nativeQuery = true)
    List<CategoryTrendResponse> findAllByOrderByRevenueDesc();

}
