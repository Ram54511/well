package com.dcode7.iwell.CNF;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDto {

    private UUID id;

    private User requestedTo;

    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

    private List<InventoryRequestDetailDto> inventoryRequestDetail;
}
