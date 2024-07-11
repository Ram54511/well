package com.dcode7.iwell.user.invoice.taxrates;


import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.returninventoryitem.ReturnInventoryItem;
import com.dcode7.iwell.user.inventory.returninventoryitem.ReturnStatus;
import com.dcode7.iwell.user.invoice.taxrates.service.TaxService;
import com.dcode7.iwell.utils.ErrorCollectionUtil;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@Tag(name = "Tax Managment")
public class TaxController {
    private final LocalService localService;
    private final TaxService taxService;

    @PostMapping(value = URLConstant.TAX)
    public ResponseEntity<Object> createTax(@Valid @RequestBody TaxDTO taxDTO,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }
        TaxRate taxRate = taxService.createTax(taxDTO);
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("tax.created"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, taxRate);
    }

    @PutMapping(value = URLConstant.TAX_UPDATE + "{taxId}")
    public ResponseEntity<Object> updateTax(@PathVariable("taxId") UUID taxId,
                                                                   @Valid @RequestBody TaxDTO taxDTO,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }
        TaxRate taxRate = taxService.updateRate(taxId,taxDTO);
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("tax.updated"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, taxRate);
    }

    @GetMapping(value = URLConstant.GET_ALL_TAX)
    public ResponseEntity<Object> getInventoryCategoryById() {
       List<TaxRate> tax = taxService.getallTypesOfTax();
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("tax.found.success"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, tax);
    }
}
