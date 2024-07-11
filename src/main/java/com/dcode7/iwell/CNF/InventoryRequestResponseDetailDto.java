package com.dcode7.iwell.CNF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestResponseDetailDto {
    private Integer quantity;
    private BigDecimal totalAmount;
    private InventoryItemDTO inventoryItemDTO;
}
