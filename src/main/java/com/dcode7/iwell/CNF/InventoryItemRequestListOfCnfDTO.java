package com.dcode7.iwell.CNF;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemRequestListOfCnfDTO {

    private CNFUserRequestDto requestedBy;

    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

    private List<InventoryRequestDetailDto> inventoryRequestDetail;
}
