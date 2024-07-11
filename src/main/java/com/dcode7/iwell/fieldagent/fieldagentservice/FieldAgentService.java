package com.dcode7.iwell.fieldagent.fieldagentservice;

import com.dcode7.iwell.fieldagent.CNFInventoryItemDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestDTO;

import java.util.List;
import java.util.UUID;

public interface FieldAgentService {

    List<CNFInventoryItemDTO> getInventoryItemOfCnf();

    void approvedRequestByCNF(UUID id, InventoryStatus inventoryStatus);

    InventoryRequest createRequest(InventoryRequestDTO inventoryRequest);
}
