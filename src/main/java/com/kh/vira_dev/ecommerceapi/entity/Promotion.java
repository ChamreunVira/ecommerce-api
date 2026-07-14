package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.PromotionStatus;
import com.kh.vira_dev.ecommerceapi.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_promotion")
@Getter
@Setter
public class Promotion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code" , nullable = false , unique = true , length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "promtion_type" , nullable = false)
    private PromotionType type;

    @Column(name = "value" , precision = 10 , scale = 2)
    private BigDecimal value;

    @Column(name = "minimum_order" , precision = 10, scale = 2)
    private BigDecimal minimumOrder;

    @Column(name = "usage_count")
    private Integer usageCount;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_status" , nullable = false)
    private PromotionStatus status;

    private LocalDateTime startAt;

    private LocalDateTime expiryAt;

}
