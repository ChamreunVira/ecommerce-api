package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.AddItemRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UpdateItemRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CartResponse;
import com.kh.vira_dev.ecommerceapi.entity.Cart;

public interface CartService {

    CartResponse addItem(AddItemRequest request);

    CartResponse getAll();

    CartResponse updateItem(Long itemId, UpdateItemRequest request);

    void removeItem(Long itemId);

    void clear();

    Cart getEntity();


}
