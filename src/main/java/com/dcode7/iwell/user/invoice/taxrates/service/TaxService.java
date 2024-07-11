package com.dcode7.iwell.user.invoice.taxrates.service;

import com.dcode7.iwell.user.invoice.taxrates.TaxDTO;
import com.dcode7.iwell.user.invoice.taxrates.TaxRate;

import java.util.List;
import java.util.UUID;

public interface TaxService {
    TaxRate createTax(TaxDTO taxDto);
    TaxRate updateRate(UUID id, TaxDTO taxDTO);
    List<TaxRate> getallTypesOfTax();
}
