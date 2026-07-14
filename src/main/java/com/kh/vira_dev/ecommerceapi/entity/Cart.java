package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "cart" , fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

}
