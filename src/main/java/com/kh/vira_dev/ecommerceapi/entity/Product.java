package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , nullable = false , length = 50)
    private String name;

    @Column(name = "description" , nullable = false , length = 500)
    private String description;

    @Column(name = "price" , nullable = false)
    private Double price;

    @Column(name = "discount")
    private float discountRate;

    @Column(name = "quantity" , nullable = false)
    private int qty;

    @Column(name = "reorder_point")
    private Integer reorderPoint = 10;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_product_image",
            joinColumns = @JoinColumn(name = "product_id" , referencedColumnName = "id")
    )
    @Column(name = "image_url")
    private List<String> image = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id" , referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "product" ,  cascade = CascadeType.ALL , fetch = FetchType.LAZY , orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Transient
    public InventoryStatus getInventoryStatus() {

        if (qty <= 0) {
            return InventoryStatus.OUT_OF_STOCK;
        }

        if (qty <= reorderPoint) {
            return InventoryStatus.LOW_STOCK;
        }

        return InventoryStatus.IN_STOCK;
    }

    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

}
