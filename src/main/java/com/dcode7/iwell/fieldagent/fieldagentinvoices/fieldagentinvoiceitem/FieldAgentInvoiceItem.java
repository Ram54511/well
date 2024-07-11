package com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem;

import com.dcode7.iwell.fieldagent.fieldagentinvoices.FieldAgentInvoice;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FieldAgentInvoiceItem {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Inventory item is required")
    @ManyToOne
    private InventoryItem inventoryItem;

    @NotNull(message = "Deal price is required")
    private BigDecimal dealPrice;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "field_agent_invoice_id")
    private FieldAgentInvoice fieldAgentInvoice;
}
