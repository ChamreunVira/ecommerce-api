package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockItemResponse {

    private Long id;

    private String name;

    private String category;

    private Integer qty;

    private Integer reorderPoint;

    private InventoryStatus status;

}
