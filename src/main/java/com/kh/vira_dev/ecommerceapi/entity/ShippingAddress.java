package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_shipping_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddress extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    @Column(name = "full_name" , nullable = false)
    private String fullName;

    @Column(name = "phone" , nullable = false)
    private String phone;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "shipping_note")
    private String note;

    @Column(name = "country")
    private String country;

    @Column(name = "is_default")
    private boolean isDefault = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

}