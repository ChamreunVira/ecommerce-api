package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tbl_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false , unique = true , length = 50)
    private String name;

    @Column(name = "description" , nullable = false , length = 255)
    private String description;

    @OneToMany(mappedBy = "category" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<Product> products;

    private Boolean status = false;

}
