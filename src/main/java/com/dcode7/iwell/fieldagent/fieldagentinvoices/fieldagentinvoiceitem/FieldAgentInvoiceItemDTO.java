package com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldAgentInvoiceItemDTO {
    private UUID InventoryItemId;
    private BigDecimal dealPrice;
    private Integer quantity;
}
