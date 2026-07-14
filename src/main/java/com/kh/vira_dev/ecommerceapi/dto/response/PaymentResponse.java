package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.CurrencyType;
import com.kh.vira_dev.ecommerceapi.enums.PaymentMethod;
import com.kh.vira_dev.ecommerceapi.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private int paymentId;

    private int orderId;

    private String orderCode;

    private String customer;

    private PaymentMethod method;

    private PaymentStatus status;

    private BigDecimal amount;

    private CurrencyType currency;

    private String transactionId;

    private String qrString;

    private String deeplink;

    private Instant expiresAt;

    private Instant paidAt;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
