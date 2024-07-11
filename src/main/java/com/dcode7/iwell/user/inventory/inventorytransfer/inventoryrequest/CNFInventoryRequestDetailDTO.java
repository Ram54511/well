package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest;

import com.dcode7.iwell.CNF.InventoryItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CNFInventoryRequestDetailDTO {
    private Integer quantity;
    private BigDecimal totalAmount;
    private InventoryItemDTO inventoryItemDTO;
}
