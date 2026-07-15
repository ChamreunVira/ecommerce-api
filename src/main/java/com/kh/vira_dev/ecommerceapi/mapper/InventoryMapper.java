package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.response.StockItemResponse;
import com.kh.vira_dev.ecommerceapi.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public StockItemResponse toResponse(Product product) {
        String categoryName = product.getCategory().getName();
        return StockItemResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .qty(product.getQty())
                .status(product.getInventoryStatus())
                .category(categoryName)
                .reserved(product.getReservedQuantity())
                .reorderPoint(product.getReorderPoint())
                .build();
    }

}
