package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tbl_carrier")
@Getter
@Setter
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , length = 50 , nullable = false)
    private String name;

    @OneToMany(mappedBy = "carrier" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Shipment> shipments;
}
