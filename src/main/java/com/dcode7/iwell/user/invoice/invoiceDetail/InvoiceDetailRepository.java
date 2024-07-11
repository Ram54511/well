package com.dcode7.iwell.user.invoice.invoiceDetail;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, UUID> {

}
