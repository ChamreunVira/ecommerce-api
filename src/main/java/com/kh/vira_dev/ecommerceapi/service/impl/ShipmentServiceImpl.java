package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.ShipmentRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ShipmentResponse;
import com.kh.vira_dev.ecommerceapi.entity.Carrier;
import com.kh.vira_dev.ecommerceapi.entity.Shipment;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.ShipmentMapper;
import com.kh.vira_dev.ecommerceapi.repository.ShipmentRepository;
import com.kh.vira_dev.ecommerceapi.service.CarrierService;
import com.kh.vira_dev.ecommerceapi.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final CarrierService carrierService;

    @Override
    @Transactional
    public ShipmentResponse create(ShipmentRequest request) {
        Shipment shipment = shipmentMapper.toEntity(request);
        Carrier carrier = carrierService.findByName(request.getCarrier())
                .orElseGet(() -> carrierService.create(request.getCarrier()));
        shipment.setCarrier(carrier);
        Shipment saved = shipmentRepository.save(shipment);
        shipment.setShipmentCode(String.format("SHP-%04d" , saved.getId()));
        return shipmentMapper.toResponse(shipment);
    }

    @Override
    public ShipmentResponse update(Long id, ShipmentRequest request) {
        Shipment shipment = findByOrThrow(id);
        shipmentMapper.applyShipmentFields(shipment, request);
        return shipmentMapper.toResponse(shipment);
    }

    @Override
    public List<ShipmentResponse> getAll() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return shipments.stream()
                .map(shipmentMapper::toResponse)
                .toList();
    }

    private Shipment findByOrThrow(Long id) {
        return shipmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Shipment")
        );
    }
}
