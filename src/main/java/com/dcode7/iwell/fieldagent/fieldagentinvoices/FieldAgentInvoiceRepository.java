package com.dcode7.iwell.fieldagent.fieldagentinvoices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldAgentInvoiceRepository extends JpaRepository<FieldAgentInvoice, UUID> {
}
