package com.dcode7.iwell.user.invoice.taxrates;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaxRepository extends JpaRepository<TaxRate, UUID> {
    TaxRate findByName(String name);
}
