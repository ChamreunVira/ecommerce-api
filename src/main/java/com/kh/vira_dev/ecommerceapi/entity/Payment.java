package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.CurrencyType;
import com.kh.vira_dev.ecommerceapi.enums.PaymentMethod;
import com.kh.vira_dev.ecommerceapi.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tbl_payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method" , nullable = false)
    private PaymentMethod method;

    @Column(name = "payment_status" , nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "transaction_id" , nullable = false , unique = true , length = 50)
    private String transactionId;

    @Column(name = "qr_string" , nullable = false , length = 500)
    private String qrString;

    @Column(name = "md5" , nullable = false , length = 500)
    private String md5;

    @Column(name = "deep_link")
    private String deepLink;

    @Column(name = "amount" , nullable = false , precision = 10 , scale = 2)
    private BigDecimal amount;

    @Column(name = "currency")
    private CurrencyType currency;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "webhook_payload" , columnDefinition = "TEXT")
    private String webhookPayload;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id" , referencedColumnName = "id")
    private Order order;

}
