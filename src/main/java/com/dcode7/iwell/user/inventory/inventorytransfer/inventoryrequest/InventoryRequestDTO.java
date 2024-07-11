package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest;

import java.util.List;
import java.util.UUID;

import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetailDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDTO {
	private List<InventoryRequestDetailDTO> inventoryRequestDetail;

}
