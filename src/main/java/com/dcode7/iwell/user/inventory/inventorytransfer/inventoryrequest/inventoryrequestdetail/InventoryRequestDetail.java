package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail;

import java.math.BigDecimal;
import java.util.UUID;

import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;

import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class InventoryRequestDetail {

	@Id
	@GeneratedValue
	private UUID id;
	private Integer quantity;

	@ManyToOne
	@ToString.Exclude
	private InventoryItem requestedItem;

	@ManyToOne
	private InventoryRequest inventoryRequest;

}
