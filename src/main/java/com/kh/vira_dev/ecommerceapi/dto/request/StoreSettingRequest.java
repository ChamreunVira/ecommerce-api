package com.kh.vira_dev.ecommerceapi.dto.request;

import com.kh.vira_dev.ecommerceapi.enums.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreSettingRequest {

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
