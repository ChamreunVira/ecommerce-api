package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.InventoryStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void getInventoryStatus_ShouldHandleNullReorderPointWithoutException() {
        Product product = new Product();
        product.setQty(5);
        product.setReorderPoint(null);

        assertEquals(10, product.getReorderPoint());
        assertEquals(InventoryStatus.LOW_STOCK, product.getInventoryStatus());
    }

    @Test
    void getInventoryStatus_ShouldReturnOutOfStockWhenQtyIsZero() {
        Product product = new Product();
        product.setQty(0);
        product.setReorderPoint(10);

        assertEquals(InventoryStatus.OUT_OF_STOCK, product.getInventoryStatus());
    }

    @Test
    void getInventoryStatus_ShouldReturnInStockWhenQtyAboveReorderPoint() {
        Product product = new Product();
        product.setQty(15);
        product.setReorderPoint(10);

        assertEquals(InventoryStatus.IN_STOCK, product.getInventoryStatus());
    }
}
