package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.entity.Cart;
import com.kh.vira_dev.ecommerceapi.entity.Order;

public interface InventoryService {

    void validateStock(Cart cart);

    void deductsStock(Cart cart);

    void restoreStock(Order order);

}
