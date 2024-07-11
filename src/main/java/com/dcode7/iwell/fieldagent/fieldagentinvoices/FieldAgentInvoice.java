package com.dcode7.iwell.fieldagent.fieldagentinvoices;


import com.dcode7.iwell.fieldagent.customer.Customer;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem.FieldAgentInvoiceItem;
import com.dcode7.iwell.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FieldAgentInvoice extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Customer customer;
    private BigDecimal totalAmount;
    private BigDecimal partialPayment;
    private BigDecimal remainingAmount;
    private BigDecimal discount;
    private String creditTerm;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToMany
    private List<FieldAgentInvoiceItem> items;


}
