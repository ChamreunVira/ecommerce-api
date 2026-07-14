package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Optional<Carrier> findByName(String name);
}
