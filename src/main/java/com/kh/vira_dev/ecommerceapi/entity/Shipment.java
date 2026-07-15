package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_shipment")
@Getter
@Setter
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipment_code" , unique = true)
    private String shipmentCode;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @Column(name = "destination")
    private String destination;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "estimated_devlivery")
    private LocalDateTime estimatedDelivery;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carrier_id" , referencedColumnName = "id")
    private Carrier carrier;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id" , referencedColumnName = "id")
    private Order order;

}
