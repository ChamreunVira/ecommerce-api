package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.ShipmentRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ShipmentResponse;
import com.kh.vira_dev.ecommerceapi.entity.Carrier;
import com.kh.vira_dev.ecommerceapi.entity.Order;
import com.kh.vira_dev.ecommerceapi.entity.Shipment;
import com.kh.vira_dev.ecommerceapi.entity.ShippingAddressSnapshot;
import com.kh.vira_dev.ecommerceapi.enums.ShipmentStatus;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.ShipmentMapper;
import com.kh.vira_dev.ecommerceapi.repository.OrderRepository;
import com.kh.vira_dev.ecommerceapi.repository.ShipmentRepository;
import com.kh.vira_dev.ecommerceapi.service.CarrierService;
import com.kh.vira_dev.ecommerceapi.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.kh.vira_dev.ecommerceapi.util.Utils.generateTrackingNumber;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private static final String DEFAULT_CARRIER = "Vira Express";

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final ShipmentMapper shipmentMapper;
    private final CarrierService carrierService;

    @Override
    @Transactional
    public ShipmentResponse create(ShipmentRequest request) {
        if (request.getOrderId() == null) {
            throw new IllegalArgumentException("orderId is required.");
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order"));

        if (order.getShipment() != null) {
            throw new IllegalStateException("Order already has a shipment.");
        }

        Shipment shipment = shipmentMapper.toEntity(request);
        Carrier carrier = resolveCarrier(request.getCarrier());
        shipment.setCarrier(carrier);
        shipment.setOrder(order);

        Shipment saved = shipmentRepository.save(shipment);
        saved.setShipmentCode(String.format("SHP-%04d", saved.getId()));
        saved = shipmentRepository.save(saved);

        order.setShipment(saved);
        order.setTrackingNumber(saved.getTrackingNumber());
        orderRepository.save(order);

        return shipmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public Shipment createForOrder(Order order) {
        if (order.getShipment() != null) {
            return order.getShipment();
        }

        ShippingAddressSnapshot address = order.getShippingAddressSnapshot();
        if (address == null) {
            throw new IllegalStateException("Order shipping address is required to create a shipment.");
        }

        String country = blankToDefault(address.getCountry(), "XXX");
        String city = blankToDefault(address.getCity(), "XXX");
        String trackingNumber = generateTrackingNumber(
                padCodePart(country),
                padCodePart(city)
        );

        String destination = String.join(", ",
                blankToDefault(address.getAddressLine(), ""),
                blankToDefault(address.getCity(), ""),
                blankToDefault(address.getCountry(), "")
        ).replaceAll("(^,\\s*)|(,\\s*$)", "").replaceAll(",\\s*,", ",");

        Shipment shipment = new Shipment();
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setDestination(destination);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setCarrier(resolveCarrier(DEFAULT_CARRIER));
        shipment.setEstimatedDelivery(LocalDateTime.now().plusDays(7));
        shipment.setOrder(order);

        Shipment saved = shipmentRepository.save(shipment);
        saved.setShipmentCode(String.format("SHP-%04d", saved.getId()));
        saved = shipmentRepository.save(saved);

        order.setShipment(saved);
        order.setTrackingNumber(trackingNumber);
        orderRepository.save(order);

        return saved;
    }

    @Override
    @Transactional
    public ShipmentResponse update(Long id, ShipmentRequest request) {
        Shipment shipment = findByOrThrow(id);
        shipmentMapper.applyShipmentFields(shipment, request);

        if (request.getCarrier() != null && !request.getCarrier().isBlank()) {
            shipment.setCarrier(resolveCarrier(request.getCarrier()));
        }

        Shipment saved = shipmentRepository.save(shipment);

        if (saved.getOrder() != null) {
            saved.getOrder().setTrackingNumber(saved.getTrackingNumber());
            orderRepository.save(saved.getOrder());
        }

        return shipmentMapper.toResponse(saved);
    }

    @Override
    public List<ShipmentResponse> getAll() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return shipments.stream()
                .map(shipmentMapper::toResponse)
                .toList();
    }

    private Carrier resolveCarrier(String carrierName) {
        String name = (carrierName == null || carrierName.isBlank()) ? DEFAULT_CARRIER : carrierName.trim();
        return carrierService.findByName(name)
                .orElseGet(() -> carrierService.create(name));
    }

    private String blankToDefault(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value.trim();
    }

    private String padCodePart(String value) {
        String upper = value.toUpperCase().replaceAll("[^A-Z0-9]", "");
        if (upper.length() >= 3) {
            return upper.substring(0, 3);
        }
        return (upper + "XXX").substring(0, 3);
    }

    private Shipment findByOrThrow(Long id) {
        return shipmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Shipment")
        );
    }
}
