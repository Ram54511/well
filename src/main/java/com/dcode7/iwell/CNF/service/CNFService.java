package com.dcode7.iwell.CNF.service;

import com.dcode7.iwell.CNF.CNFUserDTO;
import com.dcode7.iwell.CNF.InventoryItemRequestListOfCnfDTO;
import com.dcode7.iwell.CNF.InventoryRequestResponseDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.CNFInventoryRequestDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CNFService {

    List<CNFUserDTO> getAllFieldAgent();

    List<InventoryRequestResponseDTO> getRequestItemListOfLoginUser(InventoryStatus status);

    CNFInventoryRequestDTO getInventoryRequestsByInventoryRequestId(UUID inventoryRequestId);

    List<InventoryItemRequestListOfCnfDTO> getAllRequestOfCNF();

}
