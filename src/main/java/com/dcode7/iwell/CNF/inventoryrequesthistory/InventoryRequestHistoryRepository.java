package com.dcode7.iwell.CNF.inventoryrequesthistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryRequestHistoryRepository extends JpaRepository<InventoryRequestHistory, UUID> {
}
