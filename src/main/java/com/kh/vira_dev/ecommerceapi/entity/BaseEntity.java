package com.kh.vira_dev.ecommerceapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_at" , nullable = false)
    private LocalDate createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;


}
