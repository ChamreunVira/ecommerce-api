package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.ProductRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse update(int id, ProductRequest request);

    void delete(int id);

    ProductResponse getById(int id);

    List<ProductResponse> getAll();

    List<ProductResponse> filterPrice(BigDecimal minPrice , BigDecimal maxPrice);
}
