package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.response.StockItemResponse;
import com.kh.vira_dev.ecommerceapi.entity.*;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.InventoryMapper;
import com.kh.vira_dev.ecommerceapi.repository.ProductRepository;
import com.kh.vira_dev.ecommerceapi.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final InventoryMapper inventoryMapper;

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

    @Override
    public List<StockItemResponse> getStockItems() {
        List<Product> products = productRepository.findAll();
        return products
                .stream()
                .map(inventoryMapper::toResponse)
                .toList();
    }

    @Override
    public StockItemResponse getStockItem(Long itemId) {
        Product product = productRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        return inventoryMapper.toResponse(product);
    }

}
