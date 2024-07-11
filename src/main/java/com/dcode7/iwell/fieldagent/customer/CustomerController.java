package com.dcode7.iwell.fieldagent.customer;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.fieldagent.customer.service.CustomerService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Field Agent Management")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    private final LocalService localService;

    @Operation(summary = "Register an customer")
    @PostMapping(value = URLConstant.CUSTOMER)
    public ResponseEntity<Object> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR, ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }
        Customer customer = customerService.createCustomer(customerDTO);
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("user.registration.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE, customer);
    }
}
