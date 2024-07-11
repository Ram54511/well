package com.dcode7.iwell.user.inventory.returninventoryitem;
import lombok.Data;

@Data
public class ReturnInventoryItemResponseDTO {
    private Boolean restockingFee;
    private Double restockingFeePercentage;
}
