package com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldAgentInvoiceItemRepository extends JpaRepository<FieldAgentInvoiceItem, UUID> {
}
