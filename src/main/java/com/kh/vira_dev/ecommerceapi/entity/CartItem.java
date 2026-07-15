package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_cart_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity" , nullable = false)
    private int quantity;

    @Column(name = "unit_price" , nullable = false , precision = 10 , scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id" , referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id" , referencedColumnName = "id")
    private Cart cart;

}
