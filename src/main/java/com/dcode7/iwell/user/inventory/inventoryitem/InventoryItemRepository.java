package com.dcode7.iwell.user.inventory.inventoryitem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {
	Optional<InventoryItem> findByIdAndDeletedFalse(UUID id);

	List<InventoryItem> findAllByDeletedFalse();

}
