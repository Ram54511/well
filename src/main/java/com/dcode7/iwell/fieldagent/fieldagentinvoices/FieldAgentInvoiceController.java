package com.dcode7.iwell.fieldagent.fieldagentinvoices;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.service.FieldAgentInvoiceService;
import com.dcode7.iwell.utils.ErrorCollectionUtil;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Field Agent Management")
@RequiredArgsConstructor
public class FieldAgentInvoiceController {

    private final FieldAgentInvoiceService fieldAgentInvoiceService;

    private final LocalService localService;

    @Operation(summary = "Generate invoices for customer")
    @PostMapping(value = URLConstant.FIELDAGENT_INVOICES)
    public ResponseEntity<Object> registerCustomer(@Valid @RequestBody FieldAgentInvoiceDTO fieldAgentInvoiceDTO, @RequestParam PaymentStatus paymentStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR, ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }
        FieldAgentInvoice fieldAgentInvoice = fieldAgentInvoiceService.createFieldAgentInvoice(fieldAgentInvoiceDTO,paymentStatus);
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("user.invoices.generate.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE, fieldAgentInvoice);
    }
}
