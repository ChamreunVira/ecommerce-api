package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.CheckoutRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UpdateOrderStatusRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.*;
import com.kh.vira_dev.ecommerceapi.entity.Cart;
import com.kh.vira_dev.ecommerceapi.entity.Order;
import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;

import java.util.List;

public interface OrderService {

  Order create(Cart cart, PricingResult pricing, CheckoutRequest request);

  List<OrderSummaryResponse> getMyOrders(OrderStatus status);

  List<OrderResponse> getAll();

  OrderResponse getDetails(Short orderId);

  OrderCancelResponse cancel(Short orderId);

  OrderStatusUpdateResponse updateStatus(Short orderId, UpdateOrderStatusRequest request);

  List<RecentOrderResponse> recent();

  List<RevenueByMonthResponse> revenueByMonth();

}
