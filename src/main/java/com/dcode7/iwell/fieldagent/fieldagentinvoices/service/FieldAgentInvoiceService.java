package com.dcode7.iwell.fieldagent.fieldagentinvoices.service;

import com.dcode7.iwell.fieldagent.fieldagentinvoices.FieldAgentInvoice;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.FieldAgentInvoiceDTO;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.PaymentStatus;

public interface FieldAgentInvoiceService {
    FieldAgentInvoice createFieldAgentInvoice(FieldAgentInvoiceDTO invoiceDTO, PaymentStatus paymentStatus);
}
