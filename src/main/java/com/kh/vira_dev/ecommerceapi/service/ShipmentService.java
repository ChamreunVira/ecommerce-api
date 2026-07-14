package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.ShipmentRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ShipmentResponse;

import java.util.List;

public interface ShipmentService {

    ShipmentResponse create(ShipmentRequest request);

    ShipmentResponse update(Long id, ShipmentRequest request);

    List<ShipmentResponse> getAll();
}
