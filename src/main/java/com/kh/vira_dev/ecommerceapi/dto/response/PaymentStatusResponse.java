package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.CurrencyType;
import com.kh.vira_dev.ecommerceapi.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatusResponse {

    private String transactionId;

    private int orderId;

    private PaymentStatus status;

    private BigDecimal amount;
    
    private CurrencyType currency;

    private Instant expiresAt;

    private Instant paidAt;

}
