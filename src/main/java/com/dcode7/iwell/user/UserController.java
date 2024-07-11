package com.dcode7.iwell.user;

import com.dcode7.iwell.user.enums.UserRequestStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.service.InventoryRequestService;
import com.dcode7.iwell.user.transaction.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.user.service.UserService;
import com.dcode7.iwell.utils.ErrorCollectionUtil;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@Tag(name = "User Management", description = "Register CNF Admin and field agent.")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocalService localService;

    @Autowired
    private InventoryRequestService inventoryRequestService;

    @Operation(summary = "Register an user",
            description = "Request by user to register account i.e field agent.")
    @PostMapping(value = URLConstant.FIELDAGENT_REGISTER)
    public ResponseEntity<Object> registerFieldAgent(@Valid @RequestBody FieldAgentSignUpDTO fieldAgentSignUpDTO,
                                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }

        User registeredUser = userService.registerFieldAgent(fieldAgentSignUpDTO);

        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("user.registration.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE,
                userService.getRegistrationSuccessDetails(registeredUser, fieldAgentSignUpDTO.getPassword()));
    }

    @Operation(summary = "Response to user to create account request i.e (Approved or Rejected)",
            description = "Update an requested user to create account status by admin.")
    @PutMapping(value = URLConstant.USER_CREATE_RESPONSE + "{userId}")
    public ResponseEntity<Object> responseForCreatingAccountFromAdmin(@PathVariable("userId") UUID userId, @RequestParam("userRequestStatus") UserRequestStatus userRequestStatus) {
        User response = userService.ResponseForCreatingAccount(userId, userRequestStatus);
        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("admin.approved"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE, response);
    }

    @Operation(summary = "Response the inventory Request i.e (Approved or Rejected).",
            description = "Request by user for inventory item.")
    @PutMapping(value = URLConstant.INVENTORY_RESPONSE + "{id}")
    public ResponseEntity<Object> getRegistrationOptions(@PathVariable("id") UUID id,
                                                         @RequestParam("status") InventoryStatus status) {
        inventoryRequestService.approvedRequest(id, status);
        return ResponseHandler.responseWithoutData(HttpStatus.OK, true,
                localService.getMessage("inventory.response.item"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE);
    }

}
