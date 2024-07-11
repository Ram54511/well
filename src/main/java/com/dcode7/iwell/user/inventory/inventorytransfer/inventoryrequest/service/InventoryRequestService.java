package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.service;

import java.util.Optional;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestDTO;

public interface InventoryRequestService {
	InventoryRequest createRequest(InventoryRequestDTO inventoryRequestDTO);

	InventoryRequest updateRequest(UUID id, InventoryRequestDTO inventoryRequestDTO);

	void approvedRequest(UUID id, InventoryStatus inventoryStatus);

	User findAdmin();

}
