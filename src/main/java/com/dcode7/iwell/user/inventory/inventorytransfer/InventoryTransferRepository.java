package com.dcode7.iwell.user.inventory.inventorytransfer;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTransferRepository extends JpaRepository<InventoryTransfer, UUID> {

}
