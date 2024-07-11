package com.dcode7.iwell.user.inventory.inventoryitem.service;

import java.util.List;
import java.util.UUID;

import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemDto;

public interface InventoryItemService {
	InventoryItem createInventoryItem(InventoryItemDto inventoryItemDto);

	InventoryItem updateInventoryItem(UUID id, InventoryItemDto inventoryItemDTO);

	InventoryItem findById(UUID id);

	List<InventoryItem> getAllInventoryItem();

	Boolean softdeleteInventoryItem(UUID id);

}
