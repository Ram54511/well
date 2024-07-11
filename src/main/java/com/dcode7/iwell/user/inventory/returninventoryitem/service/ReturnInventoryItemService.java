package com.dcode7.iwell.user.inventory.returninventoryitem.service;

import com.dcode7.iwell.user.inventory.returninventoryitem.*;

import java.util.UUID;

public interface ReturnInventoryItemService {
    ReturnInventoryItem returnInventoryItem(ReturnInventoryItemDTO returnInventoryItemDTO, ReturnType returnType);
    ReturnInventoryItem responsereturnInventoryItemFromAdmin(ReturnStatus returnStatus, UUID returnInventoryItemId);
}
