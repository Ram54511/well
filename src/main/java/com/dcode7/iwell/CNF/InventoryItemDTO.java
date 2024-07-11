package com.dcode7.iwell.CNF;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemDTO {
    private String name;
    private String descrition;
    private String HSNCode;
    private Date expiryDate;
    private String Manufacturer;
    private Date registerDate;
    private String batchNumber;
    private String storageCondition;
    private BigDecimal unitCost;
    private BigDecimal sellingPrice;
    private Integer noOfStock;
    private String stockKeepingUnit;
    private Boolean isRefundable;
    private Boolean isExchangeable;
}
