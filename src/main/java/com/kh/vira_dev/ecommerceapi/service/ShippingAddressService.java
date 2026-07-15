package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.AddressRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.AddressResponse;

import java.util.List;

public interface ShippingAddressService {

    AddressResponse create(AddressRequest request);

    AddressResponse update(Long id , AddressRequest request);

    void delete(Long id);

    AddressResponse getById(Long id);

    List<AddressResponse> getAll();

    AddressResponse setDefault(Long id);

}
