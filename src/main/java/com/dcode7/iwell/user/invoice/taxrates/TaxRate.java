package com.dcode7.iwell.user.invoice.taxrates;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaxRate {

    @Id
    @GeneratedValue
    private UUID id;
    // name of the tax (e.g., VAT, GST)
    private String name;
    // tax rate (e.g., 0.05 for 5% tax)
    private BigDecimal rate;

}
