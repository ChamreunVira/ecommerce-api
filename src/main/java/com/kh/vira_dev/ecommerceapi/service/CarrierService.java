package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.entity.Carrier;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.repository.CarrierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarrierService {

    private final CarrierRepository carrierRepository;

    public Optional<Carrier> findByName(String name) {
        return carrierRepository.findByName(name);
    }

    public Carrier findById(Long id) {
        return carrierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrier"));
    }

    public Carrier create(String name) {
        Carrier carrier = new Carrier();
        carrier.setName(name);
        return carrierRepository.save(carrier);
    }
}
