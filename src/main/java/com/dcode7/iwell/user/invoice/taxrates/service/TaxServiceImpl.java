package com.dcode7.iwell.user.invoice.taxrates.service;

import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.invoice.taxrates.TaxDTO;
import com.dcode7.iwell.user.invoice.taxrates.TaxRate;
import com.dcode7.iwell.user.invoice.taxrates.TaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {
    private final TaxRepository taxRepository;
    private final LocalService localService;

    @Override
    /**
     * Creates a new tax rate based on the provided TaxDTO.
     *
     * @param taxDto The TaxDTO containing the information for the new tax rate.
     * @return The created TaxRate entity.
     */
    public TaxRate createTax(TaxDTO taxDto) {
        TaxRate taxRate = new TaxRate();
        taxRate.setRate(taxDto.getRate());
        taxRate.setName(taxDto.getName());
        return taxRepository.save(taxRate);
    }

    @Override
    /**
     * Updates an existing tax rate based on the provided ID and TaxDTO.
     *
     * @param id     The ID of the tax rate to be updated.
     * @param taxDTO The TaxDTO containing the updated information for the tax rate.
     * @return The updated TaxRate entity.
     */
    public TaxRate updateRate(UUID id, TaxDTO taxDTO) {
        TaxRate taxRate = taxRepository.findById(id).orElseThrow(()-> new CustomException(localService.getMessage("tax.not.found"), HttpStatus.BAD_REQUEST));
        taxRate.setName(taxDTO.getName());
        taxRate.setRate(taxDTO.getRate());
        return taxRepository.save(taxRate);
    }

    @Override
    /**
     * Retrieves all types of tax rates.
     *
     * @return A list of all tax rates.
     */
    public List<TaxRate> getallTypesOfTax() {
        return taxRepository.findAll();
    }
}
