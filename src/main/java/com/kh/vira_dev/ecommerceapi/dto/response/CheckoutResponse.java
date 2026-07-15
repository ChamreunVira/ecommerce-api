package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import com.kh.vira_dev.ecommerceapi.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutResponse {

    private Long orderId;

    private String orderCode;

    private OrderStatus status;

    private PaymentMethod paymentMethod;

    private List<OrderItemResponse> orderItems;

    private BigDecimal subtotal;

    private BigDecimal shippingFee;

    private BigDecimal totalAmount;

    private ShippingAddressResponse shippingAddress;

    private String note;

    private String trackingNumber;

    private PaymentSummaryResponse paymentSummary;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Instant cancelledAt;

}
