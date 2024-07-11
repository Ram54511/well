package com.dcode7.iwell.user.inventory.returninventoryitem;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.UUID;

@Data
public class ReturnInventoryItemDTO {

    private String fullDescription;
    private UUID inventoryRequestId;
}
