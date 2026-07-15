package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.AddressRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.AddressResponse;
import com.kh.vira_dev.ecommerceapi.entity.ShippingAddress;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.ShippingAddressMapper;
import com.kh.vira_dev.ecommerceapi.repository.ShippingAddressRepository;
import com.kh.vira_dev.ecommerceapi.service.ShippingAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingAddressMapper shippingAddressMapper;

    @Override
    public AddressResponse create(AddressRequest request) {
        ShippingAddress address = shippingAddressMapper.toEntity(request);
        ShippingAddress saved = shippingAddressRepository.save(address);
        return shippingAddressMapper.toResponse(saved);
    }

    @Override
    public AddressResponse update(Long id , AddressRequest request) {
        ShippingAddress address = findByOrThrow(id);
        shippingAddressMapper.applyShippingAddressFields(address , request);
        ShippingAddress saved = shippingAddressRepository.save(address);
        return shippingAddressMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        ShippingAddress address = findByOrThrow(id);
        log.info("Delete shipping address with id {}", id);
        shippingAddressRepository.delete(address);
    }

    @Override
    public AddressResponse getById(Long id) {
        ShippingAddress address = findByOrThrow(id);
        return shippingAddressMapper.toResponse(address);
    }

    @Override
    public List<AddressResponse> getAll() {
        List<ShippingAddress> shippingAddresses = shippingAddressRepository.findAll();
        return shippingAddresses
                .stream()
                .map(shippingAddressMapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse setDefault(Long id) {
        ShippingAddress address = findByOrThrow(id);
        address.setDefault(true);
        ShippingAddress saved = shippingAddressRepository.save(address);
        return shippingAddressMapper.toResponse(saved);
    }
    
    private ShippingAddress findByOrThrow(Long id) {
        return shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address"));
    }
}
