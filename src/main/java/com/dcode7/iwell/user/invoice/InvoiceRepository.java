package com.dcode7.iwell.user.invoice;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

}
