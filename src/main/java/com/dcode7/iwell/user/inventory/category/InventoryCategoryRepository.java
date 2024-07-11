package com.dcode7.iwell.user.inventory.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryCategoryRepository extends JpaRepository<InventoryCategory, UUID> {

	@Query("SELECT d FROM InventoryCategory d where deleted = false")
	List<InventoryCategory> findAll();

	Optional<InventoryCategory> findByIdAndDeletedFalse(UUID id);
}
