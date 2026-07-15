package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.ShipmentRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ShipmentResponse;
import com.kh.vira_dev.ecommerceapi.entity.Shipment;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {

    public Shipment toEntity(ShipmentRequest request) {
        Shipment shipment = new Shipment();
        applyShipmentFields(shipment, request);
        return shipment;
    }

    public ShipmentResponse toResponse(Shipment shipment) {
        return ShipmentResponse
                .builder()
                .id(shipment.getId())
                .code(shipment.getShipmentCode())
                .orderCode(shipment.getOrder().getOrderCode())
                .customer(shipment.getOrder().getShippingAddressSnapshot().getFullName())
                .status(shipment.getStatus())
                .destination(shipment.getDestination())
                .trackingNumber(shipment.getTrackingNumber())
                .estimatedDelivery(shipment.getEstimatedDelivery())
                .carrier(shipment.getCarrier() != null ? shipment.getCarrier().getName() : null)
                .build();
    }

    public void applyShipmentFields(Shipment shipment, ShipmentRequest request) {
        shipment.setStatus(request.getStatus());
        shipment.setDestination(request.getDestination());
        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setEstimatedDelivery(request.getEstimatedDelivery());
    }


}
