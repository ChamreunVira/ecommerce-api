package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.CheckoutRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CheckoutResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PricingResult;
import com.kh.vira_dev.ecommerceapi.entity.Cart;
import com.kh.vira_dev.ecommerceapi.entity.Order;
import com.kh.vira_dev.ecommerceapi.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CheckoutOrchestrator {

    private final CartService cartService;
    private final PricingService pricingService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final OrderMapper orderMapper;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {

        Cart cart = cartService.getEntity();

        inventoryService.validateStock(cart);

        PricingResult pricing = pricingService.calculate(cart, request.getCouponCode());

        Order order = orderService.create(cart, pricing, request);

        shipmentService.createForOrder(order);

        inventoryService.deductsStock(cart);

        cartService.clear();

        return toResponse(order);
    }

    private CheckoutResponse toResponse(Order order) {
        return CheckoutResponse
                .builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .subtotal(order.getSubtotal() != null ? order.getSubtotal().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .shippingFee(order.getShippingFee() != null ? order.getShippingFee() : BigDecimal.ZERO)
                .totalAmount(order.getTotalAmount() != null ? order.getTotalAmount().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .orderItems(order.getOrderItems().stream().map(orderMapper::toOrderItemResponse).toList())
                .shippingAddress(order.getShippingAddressSnapshot() != null
                        ? orderMapper.toShippingAddressResponse(order.getShippingAddressSnapshot())
                        : null)
                .paymentSummary(order.getPayment() != null ? orderMapper.toPaymentSummaryResponse(order.getPayment()) : null)
                .note(order.getNote())
                .trackingNumber(order.getTrackingNumber())
                .updatedAt(order.getUpdatedAt())
                .createdAt(order.getCreatedAt())
                .cancelledAt(order.getCancelledAt())
                .build();
    }

}
