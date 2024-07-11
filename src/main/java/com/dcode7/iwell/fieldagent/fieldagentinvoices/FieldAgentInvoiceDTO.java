package com.dcode7.iwell.fieldagent.fieldagentinvoices;

import com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem.FieldAgentInvoiceItemDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldAgentInvoiceDTO {

    @NotNull(message = "Customer is required")
    private UUID customerId;
    private BigDecimal partialPayment;
    private String creditTerm;
    private List<FieldAgentInvoiceItemDTO> items;
}
