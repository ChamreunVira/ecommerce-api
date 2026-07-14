package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingAddressSnapshot {

    @Column(name = "ship_full_name" , length = 100)
    private String fullName;

    @Column(name = "ship_phone")
    private String phone;

    @Column(name = "ship_order_line" , length = 255)
    private String addressLine;

    @Column(name = "ship_city" , length = 100)
    private String city;

    @Column(name = "ship_country" , length = 10)
    private String country;

}
