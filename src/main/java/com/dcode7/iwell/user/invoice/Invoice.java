package com.dcode7.iwell.user.invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.invoice.invoiceDetail.InvoiceDetail;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Invoice {
	@Id
	@GeneratedValue
	private UUID id;

	private Date invoiceDate;

	@Enumerated(EnumType.STRING)
	private InvoiceStatus status;

	private BigDecimal totalAmount;

	@ManyToOne
	private User user;

	@OneToMany(cascade = CascadeType.ALL)
	private List<InvoiceDetail> invoiceDetails;

}
