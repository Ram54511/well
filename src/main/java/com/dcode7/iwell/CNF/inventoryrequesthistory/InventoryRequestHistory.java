package com.dcode7.iwell.CNF.inventoryrequesthistory;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InventoryRequestHistory extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private InventoryRequest inventoryRequest;

    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;

    private String updatedBy;

    @ManyToOne
    private User statusUpdateBy;
}
