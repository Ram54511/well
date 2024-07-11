package com.dcode7.iwell.user.inventory.returninventoryitem;

import java.util.Date;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.utils.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReturnInventoryItem extends BaseEntity {

	@Id
	@GeneratedValue
	private UUID id;
	private String fullDescription;
	private Date returnDate;
	@Enumerated(EnumType.STRING)
	private ReturnType returnType;
	@ManyToOne
	private User user;
	private Boolean restockingFee;
	private Double restockingFeePercentage;
	@ManyToOne
	private InventoryRequest inventoryRequest;
	@Enumerated(EnumType.STRING)
	private ReturnStatus returnStatus;

}
