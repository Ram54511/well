package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRequestDetailRepository extends JpaRepository<InventoryRequestDetail, UUID> {

    Optional<InventoryRequestDetail> findByInventoryRequestAndRequestedItem(InventoryRequest inventoryRequest, InventoryItem requestedItem);

    List<InventoryRequestDetail> findByInventoryRequest(InventoryRequest inventoryRequest);
    List<InventoryRequestDetail> findByRequestedItem(InventoryItem inventoryItem);
}
