package com.dcode7.iwell.fieldagent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CNFInventoryItemDTO {

    private UUID inventoryRequest;

    private UUID inventoryId;

    private String cnf;

    private String name;

    private String description;

    private String HSNCode;

    private Date expiryDate;

    private String manufacturer;

    private Date registerDate;

    private BigDecimal unitCost;

    private BigDecimal sellingPrice;

    private Integer noOfStock;

    private String stockKeepingUnit;

    private String storageCondition;

}
