package com.dcode7.iwell.user.inventory.inventorytransfer;

import java.util.Date;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.utils.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InventoryTransfer extends BaseEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne
	private InventoryItem item;

	@ManyToOne
	private User fromUser;

	@ManyToOne
	private User toUser;

	private Integer quantity;

	private Date transferDate;

	private UserType fromUserType;

}
