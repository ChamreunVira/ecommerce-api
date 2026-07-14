package com.kh.vira_dev.ecommerceapi.specification;

import com.kh.vira_dev.ecommerceapi.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price") , minPrice , maxPrice));
    }

}
