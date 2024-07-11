package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetail;
import com.dcode7.iwell.utils.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InventoryRequest extends BaseEntity {

	@Id
	@GeneratedValue
	private UUID requestId;

	@ManyToOne
	private User requestedBy;

	@ManyToOne
	private User requestedTo;

	@Enumerated(EnumType.STRING)
	private InventoryStatus inventoryStatus;


	@OneToMany(cascade = CascadeType.ALL,mappedBy = "inventoryRequest",fetch = FetchType.EAGER)
	@JsonIgnore
	private List<InventoryRequestDetail> inventoryRequestDetail;

}
