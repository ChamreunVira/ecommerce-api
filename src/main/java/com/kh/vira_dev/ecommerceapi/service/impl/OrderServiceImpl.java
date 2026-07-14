package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.CheckoutRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UpdateOrderStatusRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.*;
import com.kh.vira_dev.ecommerceapi.entity.*;
import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.OrderMapper;
import com.kh.vira_dev.ecommerceapi.repository.CartRepository;
import com.kh.vira_dev.ecommerceapi.repository.OrderRepository;
import com.kh.vira_dev.ecommerceapi.repository.ProductRepository;
import com.kh.vira_dev.ecommerceapi.repository.ShippingAddressRepository;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartRepository cartRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;

    //block cancel
    private static final Set<OrderStatus> CANCELLABLE_STATUSES = Set.of(OrderStatus.PENDING , OrderStatus.PROCESSING , OrderStatus.SHIPPED);

    @Override
    @Transactional
    public Order create(Cart cart, PricingResult pricing, CheckoutRequest request) {
        Order order = orderMapper.builderOrder(request);

        User user = cart.getUser();
        order.setUser(user);

        ShippingAddress shippingAddress = shippingAddressRepository.findById(request.getShippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address"));
        if (shippingAddress.getUser() == null
                || !shippingAddress.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Shipping address does not belong to the current user.");
        }
        order.setShippingAddressSnapshot(orderMapper.builderShippingAddressSnapshot(shippingAddress));

        List<OrderItem> orderItems = cart.getCartItems()
                .stream()
                .map(cartItem -> orderMapper.builderOrderItemFromCartItem(cartItem, order))
                .toList();
        order.setOrderItems(orderItems);
        order.setSubtotal(pricing.getSubtotal());
        order.setShippingFee(pricing.getShippingFee());
        order.setTotalAmount(pricing.getTotalAmount());
        return orderRepository.save(order);
    }

    @Override
    public List<OrderSummaryResponse> getMyOrders(OrderStatus orderStatus) {
        User user = authService.authenticated();
        List<Order> orders = orderStatus != null
                ? orderRepository.findByUserAndOrderStatus(user , orderStatus)
                : orderRepository.findByUser(user);
        return orders.stream()
                .map(orderMapper::toOrderSummaryResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse getDetails(Short orderId) {
        User user = authService.authenticated();
        Order order = orderRepository.findByIdAndUser(orderId , user)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderCancelResponse cancel(Short orderId) {
        User user = authService.authenticated();
        Order order = orderRepository.findByIdAndUser(orderId , user)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));

        if(!CANCELLABLE_STATUSES.contains(order.getOrderStatus())) {
            throw new IllegalArgumentException("Order cannot be cancelled.");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        //increment stock back
        for(OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setQty(product.getQty() + orderItem.getQuantity());
            productRepository.save(product);
        }

        order.setCancelledAt(Instant.now());
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderCancelResponse(saved);
    }

    @Override
    public OrderStatusUpdateResponse updateStatus(Short orderId, UpdateOrderStatusRequest request) {
        Order order = findByOrThrow(orderId);
        order.setOrderStatus(request.getStatus());
        order.setTrackingNumber(order.getTrackingNumber());
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderStatusUpdateResponse(saved);
    }

    @Override
    public List<RecentOrderResponse> recent() {
        List<Order> orders = orderRepository.findTop5ByOrderByCreatedAtDesc();
        return orders.stream()
                .map(orderMapper::toRecentOrderResponse)
                .toList();
    }

    @Override
    public List<RevenueByMonthResponse> revenueByMonth() {
        return orderRepository.getRevenueByMonth()
                .stream()
                .map(revenue -> RevenueByMonthResponse.builder()
                            .month((int) revenue[0])
                            .revenue((BigDecimal) revenue[1])
                            .orders((Long) revenue[2])
                            .build()
                ).toList();
    }

    private Order findByOrThrow(Short orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));
    }

}