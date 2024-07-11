package com.dcode7.iwell.user.invoice.invoiceDetail;

import java.math.BigDecimal;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.invoice.taxrates.TaxRate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetail {

	@Id
	@GeneratedValue
	private UUID id;

	private BigDecimal invoiceAmount;

	@ManyToOne
	private InventoryItem inventoryItem;

	private Integer invoiceQuantity;

}
