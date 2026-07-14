package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.entity.*;
import com.kh.vira_dev.ecommerceapi.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Override
    public void validateStock(Cart cart) {
        for(CartItem cartItem: cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if(product.getQty() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Product " + product.getName() + " has only " + product.getQty() + " unit(s) left in stock.");
            }
        }
    }

    @Override
    public void deductsStock(Cart cart) {
        for(CartItem cartItem: cart.getCartItems()) {
            Product product = cartItem.getProduct();
            product.setQty(product.getQty() - cartItem.getQuantity());
        }
    }

    @Override
    public void restoreStock(Order order) {
        for(OrderItem orderItem: order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setQty(product.getQty() + orderItem.getQuantity());
        }
    }

}
