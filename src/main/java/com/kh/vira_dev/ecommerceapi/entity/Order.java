package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import com.kh.vira_dev.ecommerceapi.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code" , nullable = false , unique = true , length = 30)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status" ,  nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING_PAYMENT;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method" , nullable = false , length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "subtotal" , nullable = false , precision = 10 , scale = 2)
    private BigDecimal subtotal;

    @Column(name = "shipping_fee" , nullable = false , precision = 10 , scale = 2)
    private BigDecimal shippingFee;

    @Column(name = "total_amount" , nullable = false , precision = 10 , scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "note" , nullable = false , length = 255)
    private String note;

    @Column(name = "tracking_numer" , length = 255)
    private String trackingNumber;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Embedded
    private ShippingAddressSnapshot shippingAddressSnapshot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "order" , fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private Payment payment;

    @OneToOne(mappedBy = "order" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promotion_id" , referencedColumnName = "id")
    private Promotion promotion;
}
