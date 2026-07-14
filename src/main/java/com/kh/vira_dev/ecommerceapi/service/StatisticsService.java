package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.response.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CategoryService categoryService;
    private final OrderService orderService;

    public StatisticsResponse getStatistics() {
        var trendCategories = categoryService.getTrend();
        var recentOrders = orderService.recent();
        var revenueByMonth = orderService.revenueByMonth();

        return StatisticsResponse.builder()
                .recentOrders(recentOrders)
                .trendCategories(trendCategories)
                .revenueByMonths(revenueByMonth)
                .build();
    }

}
