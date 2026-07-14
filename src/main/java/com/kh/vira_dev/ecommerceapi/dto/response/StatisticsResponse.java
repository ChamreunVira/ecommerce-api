package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsResponse {

    List<RevenueByMonthResponse> revenueByMonths;

    List<CategoryTrendResponse> trendCategories;

    List<RecentOrderResponse> recentOrders;

}
