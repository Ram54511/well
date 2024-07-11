package com.dcode7.iwell.user.inventory.inventoryitem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.dcode7.iwell.user.inventory.category.InventoryCategory;
import com.dcode7.iwell.user.invoice.taxrates.TaxRate;
import com.dcode7.iwell.utils.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InventoryItem extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue
	private UUID id;
	@NotNull(message = "{inventory.item.name.required}")
	private String name;
	@NotNull(message = "{inventory.item.description.required}")
	private String descrition;
	@NotNull(message = "{inventory.item.HSNCode.required}")
	private String HSNCode;
	private Date expiryDate;
	private String Manufacturer;
	private Date registerDate;
	private String batchNumber;
	private String storageCondition;
	private BigDecimal unitCost;
	private BigDecimal sellingPrice;
	private Integer noOfStock;
	private String stockKeepingUnit;
	private Boolean deleted;
	private Boolean isRefundable;
	private Boolean isExchangeable;
	@ManyToOne
	private InventoryCategory inventoryCategory;

	@ManyToOne
	private TaxRate taxRate;

}
