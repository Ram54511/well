package com.dcode7.iwell.user.inventory.category.service;

import java.util.List;
import java.util.UUID;

import com.dcode7.iwell.user.inventory.category.InventoryCategory;
import com.dcode7.iwell.user.inventory.category.InventoryCategoryDTO;

public interface InventoryCategoryService {
	InventoryCategory createCategory(InventoryCategoryDTO inventoryCategoryDTO);

	InventoryCategory updateCategory(UUID id, InventoryCategoryDTO inventoryCategoryDTO);

	InventoryCategory getCategory(UUID id);

	List<InventoryCategory> getAllCategory();

	Boolean softDeleteInventoryCategory(UUID id);

}
