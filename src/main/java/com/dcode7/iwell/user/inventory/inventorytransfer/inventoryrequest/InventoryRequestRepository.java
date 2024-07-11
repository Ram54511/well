package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, UUID> {

    List<InventoryRequest> getInventoryRequestsByRequestedByAndInventoryStatus(User user, InventoryStatus status);

    InventoryRequest findByRequestId(UUID inventoryRequestId);

    List<InventoryRequest> getInventoryRequestItemByRequestedByAndInventoryStatus(User user, InventoryStatus status);

    List<InventoryRequest> getInventoryRequestByRequestedTo(User User);
    InventoryRequest getInventoryByRequestedTo(User User);

    InventoryRequest getInventoryRequestByRequestedBy(User User);
    InventoryRequest getInventoryRequestByRequestedByAndInventoryStatus(User User,InventoryStatus status);

}
