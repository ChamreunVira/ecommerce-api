package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_banner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "subtitle", length = 500)
    private String subtitle;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "button_label", length = 100)
    private String buttonLabel;

    @Column(name = "button_link", length = 500)
    private String buttonLink;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @PrePersist
    public void init() {
        if (this.active == null) this.active = true;
        if (this.sortOrder == null) this.sortOrder = 0;
    }
}
