package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest;


import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CNFInventoryRequestDTO {

    private InventoryStatus inventoryStatus;

    private List<CNFInventoryRequestDetailDTO> inventoryRequestDetail = new ArrayList<>();
}
