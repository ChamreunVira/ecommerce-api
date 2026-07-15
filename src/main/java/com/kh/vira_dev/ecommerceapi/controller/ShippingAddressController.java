package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.AddressRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.AddressResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.ShippingAddressService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAll() {
        var response = shippingAddressService.getAll();
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getById(@PathVariable Long id) {
        var response = shippingAddressService.getById(id);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> create(@Valid @RequestBody AddressRequest request) {
        var response = shippingAddressService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> update(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        var response = shippingAddressService.update(id , request);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        shippingAddressService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success(null));
    }

    @PutMapping("/is-default/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateIsDefault(@PathVariable Long id) {
        var response = shippingAddressService.setDefault(id);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }


}
