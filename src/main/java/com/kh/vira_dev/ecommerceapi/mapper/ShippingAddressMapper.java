package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.AddressRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.AddressResponse;
import com.kh.vira_dev.ecommerceapi.entity.ShippingAddress;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShippingAddressMapper {

    private final AuthService authService;

    public ShippingAddress toEntity(AddressRequest request) {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setUser(authService.authenticated());
        applyShippingAddressFields(shippingAddress, request);
        return shippingAddress;
    }

    public AddressResponse toResponse(ShippingAddress address) {
        return AddressResponse
                .builder()
                .addressId(address.getId())
                .fullName(address.getFullName())
                .phone(address.getPhone())
                .addressLine(address.getAddressLine())
                .province(address.getProvince())
                .city(address.getCity())
                .addressLine(address.getAddressLine())
                .country(address.getCountry())
                .note(address.getNote())
                .isDefault(address.isDefault())
                .build();
    }

    public void applyShippingAddressFields(ShippingAddress address, AddressRequest request) {
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setAddressLine(request.getAddressLine());
        address.setCountry(request.getCountry());
        address.setNote(request.getNote());
        address.setDefault(request.isDefault());
    }

}
