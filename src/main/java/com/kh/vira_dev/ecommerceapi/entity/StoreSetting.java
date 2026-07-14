package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_store_setting")
@Getter
@Setter
public class StoreSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String supportEmail;
    private String phone;
    private String address;

    private CurrencyType currency;
    private String timeZone;

    private Integer lowStockAlert;
    private BigDecimal freeShippingMinimum;
    private BigDecimal taxRate;

    private Boolean notificationNewOrders;
    private Boolean notificationLowStock;
    private Boolean notificationWeeklyReport;

    private Boolean paymentCod;
    private Boolean paymentKhqr;
    private Boolean paymentCard;

}
