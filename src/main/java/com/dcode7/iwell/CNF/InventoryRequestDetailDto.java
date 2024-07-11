package com.dcode7.iwell.CNF;

import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDetailDto {

    private Integer quantity;
    private InventoryItemDto requestedItem;
}
