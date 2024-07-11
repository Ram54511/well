package com.dcode7.iwell.CNF;

import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestResponseDTO {
    private UserInventoryRequestDTO  userInventoryRequestDTO;

    private InventoryStatus inventoryStatus;

    private List<InventoryRequestResponseDetailDto> inventoryRequestDetail;
}
