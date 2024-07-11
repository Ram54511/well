package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDetailDTO {

	@NotNull(message = "Quantity is required")
	@Min(value = 1, message = "Quantity must be greater than or equal to 1")
	private Integer quantity;

	@NotNull(message = "Inventory item ID is required")
	private UUID inventoryItemId;

}
