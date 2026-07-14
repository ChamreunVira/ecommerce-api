package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.CheckoutRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.*;
import com.kh.vira_dev.ecommerceapi.entity.*;
import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import static com.kh.vira_dev.ecommerceapi.util.Utils.generateOrderCode;

@Component
public class OrderMapper {
    public OrderResponse toResponse(Order order) {
        return OrderResponse
                .builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .subtotal(order.getSubtotal() != null ? order.getSubtotal().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .shippingFee(order.getShippingFee() != null ? order.getShippingFee() : BigDecimal.ZERO)
                .totalAmount(order.getTotalAmount() != null ? order.getTotalAmount().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .orderItems(order.getOrderItems().stream().map(this::toOrderItemResponse).toList())
                .shippingAddress(toShippingAddressResponse(order.getShippingAddressSnapshot()))
                .paymentSummary(order.getPayment() != null ? toPaymentSummaryResponse(order.getPayment()) : null)
                .note(order.getNote())
                .trackingNumber(order.getTrackingNumber())
                .updatedAt(order.getUpdatedAt())
                .createdAt(order.getCreatedAt())
                .cancelledAt(order.getCancelledAt())
                .build();

    }

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse
                .builder()
                .orderItemId(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .imageUrl(orderItem.getProduct().getImage().getFirst())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .discountRate(orderItem.getDiscountRate())
                .finalPrice(orderItem.getFinalPrice())
                .quantity(orderItem.getQuantity())
                .subtotal(orderItem.getSubtotal())
                .build();
    }

    public ShippingAddressResponse toShippingAddressResponse(ShippingAddressSnapshot shippingAddress) {
        return ShippingAddressResponse
                .builder()
                .fullName(shippingAddress.getFullName())
                .phone(shippingAddress.getPhone())
                .country(shippingAddress.getCountry())
                .city(shippingAddress.getCity())
                .addressLine(shippingAddress.getAddressLine())
                .build();
    }

    public OrderSummaryResponse toOrderSummaryResponse(Order order) {
        return OrderSummaryResponse
                .builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .totalItems(order.getOrderItems().size())
                .primaryImage(order.getOrderItems().getFirst().getProductImage())
                .createdAt(order.getCreatedAt())
                .build();
    }

    public OrderCancelResponse toOrderCancelResponse(Order order) {
        return OrderCancelResponse.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getOrderStatus())
                .cancelledAt(order.getCancelledAt())
                .build();
    }

    public OrderStatusUpdateResponse toOrderStatusUpdateResponse(Order order) {
        return OrderStatusUpdateResponse.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getOrderStatus())
                .trackingNumber(order.getTrackingNumber())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public PaymentSummaryResponse toPaymentSummaryResponse(Payment payment) {
        return PaymentSummaryResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .paymentStatus(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paidAt(payment.getPaidAt() != null ? Instant.from(payment.getPaidAt()) : null)
                .build();
    }

    public Order builderOrder(CheckoutRequest request) {
        Order order = new Order();
        applyOrderFields(order, request);
        return order;
    }

    private void applyOrderFields(Order order , CheckoutRequest request) {
        order.setOrderCode(generateOrderCode());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        order.setNote((request.getNote() == null || request.getNote().isBlank()) ? "" : request.getNote());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setCancelledAt(null);
    }

    public OrderItem builderOrderItemFromCartItem(CartItem cartItem, Order order) {

        Product product = cartItem.getProduct();

        OrderItem item = new OrderItem();

        item.setOrder(order);
        item.setProduct(product);

        item.setProductName(product.getName());
        item.setProductImage(product.getImage().getFirst());

        item.setQuantity(cartItem.getQuantity());

        item.setUnitPrice(cartItem.getUnitPrice());

        item.setDiscountRate(product.getDiscountRate());

        BigDecimal rate = BigDecimal.valueOf(product.getDiscountRate())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        BigDecimal finalPrice = cartItem.getUnitPrice()
                .multiply(BigDecimal.ONE.subtract(rate));

        item.setFinalPrice(finalPrice);

        item.setSubtotal(
                finalPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()))
        );

        return item;
    }

    public ShippingAddressSnapshot builderShippingAddressSnapshot(ShippingAddress request) {
        ShippingAddressSnapshot snapshot = new ShippingAddressSnapshot();
        applyShippingAddressFields(snapshot, request);
        return snapshot;
    }

    public RecentOrderResponse toRecentOrderResponse(Order order) {
        return RecentOrderResponse.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .fullName(order.getUser().getFullName())
                .totalAmount(order.getTotalAmount())
                .createdDate(order.getCreatedAt())
                .status(order.getOrderStatus())
                .build();
    }

    private void applyShippingAddressFields(ShippingAddressSnapshot snapshot, ShippingAddress request) {
        snapshot.setFullName(request.getFullName());
        snapshot.setPhone(request.getPhone());
        snapshot.setCountry(request.getCountry());
        snapshot.setCity(request.getCity());
        snapshot.setAddressLine(request.getAddressLine());
    }
}
