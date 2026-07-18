package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_review")
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;

    private String comment;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id" , referencedColumnName = "id")
    private Product product;

}
