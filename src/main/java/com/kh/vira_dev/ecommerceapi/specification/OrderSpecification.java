package com.kh.vira_dev.ecommerceapi.specification;

import com.kh.vira_dev.ecommerceapi.dto.response.RecentOrderResponse;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class OrderSpecification {

//    public static Specification<List<RecentOrderResponse>> findRecentOrder() {
//        return ((root, query, criteriaBuilder) -> {
//            root.fetch("user" , JoinType.INNER);
//
//            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
//
//            return criteriaBuilder.conjunction();
//        });
//    }

}
