package com.dcode7.iwell.user.inventory.returninventoryitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReturnInventoryItemRepository extends JpaRepository<ReturnInventoryItem, UUID> {

}
