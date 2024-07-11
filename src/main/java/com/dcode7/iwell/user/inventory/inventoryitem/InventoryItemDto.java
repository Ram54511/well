package com.dcode7.iwell.user.inventory.inventoryitem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemDto {

	@NotNull(message = "{inventory.item.name.required}")
	private String name;

	@NotNull(message = "{inventory.item.description.required}")
	private String description;

	@NotNull(message = "{inventory.item.HSNCode.required}")
	private String HSNCode;

	@NotNull(message = "{inventory.item.expiryDate.required}")
	private Date expiryDate;

	@NotNull(message = "{inventory.item.manufacturer.required}")
	private String manufacturer;

	@NotNull(message = "{inventory.item.registerDate.required}")
	private Date registerDate;

	@NotNull(message = "{inventory.item.unitCost.required}")
	private BigDecimal unitCost;

	@NotNull(message = "{inventory.item.sellingPrice.required}")
	private BigDecimal sellingPrice;

	@NotNull(message = "{inventory.item.noOfStock.required}")
	private Integer noOfStock;

	@NotNull(message = "{inventory.item.stockKeepingUnit.required}")
	private String stockKeepingUnit;

	@NotNull(message = "{inventory.item.inventoryCategoryId.required}")
	private UUID inventoryCategoryId;

	private Boolean isRefundable;

	private Boolean isExchangeable;

	private String batchNumber;

	private String storageCondition;

	private UUID taxId;

}
