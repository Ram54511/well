package com.dcode7.iwell.CNF;

import com.dcode7.iwell.CNF.service.CNFService;
import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.fieldagent.CNFInventoryItemDTO;
import com.dcode7.iwell.fieldagent.fieldagentservice.FieldAgentService;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.UserSignUpDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.CNFInventoryRequestDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.service.InventoryRequestService;
import com.dcode7.iwell.user.service.UserService;
import com.dcode7.iwell.user.transaction.enums.PaymentMethod;
import com.dcode7.iwell.utils.ErrorCollectionUtil;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@Tag(name = "CNF Management")
public class CNFUserController {

    private final LocalService localService;

    private final InventoryRequestService inventoryRequestService;

    private final CNFService cnfService;

    private final UserService userService;

    private final FieldAgentService fieldAgentService;

    @Operation(summary = "Get All fieldAgent of cnf")
    @GetMapping(value = URLConstant.LOGIN_CNF_FIELDAGENT)
    public ResponseEntity<Object> getAllFieldAgentOfLoginCnf() {
        List<CNFUserDTO> fieldAgentUser = cnfService.getAllFieldAgent();
        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("field.agent.found.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE, fieldAgentUser);
    }

    @Operation(summary = "Get All inventory request of Login cnf")
    @GetMapping(value = URLConstant.LOGIN_CNF_REQUEST)
    public ResponseEntity<Object> getAllInventroyRequestOfLoginCnf(@RequestParam InventoryStatus status) {
        List<InventoryRequestResponseDTO> inventoryListOfCNF = cnfService.getRequestItemListOfLoginUser(status);
        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("inventory.request.found.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryListOfCNF);
    }

    @Schema(name = "Requested person can update the request for inventory item")
    @PutMapping(value = URLConstant.UPDATE_INVENTORY_REQUEST + "{id}")
    public ResponseEntity<Object> updateInventoryRequest(@PathVariable("id") UUID id,
                                                         @RequestBody InventoryRequestDTO inventoryRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }

        InventoryRequest inventoryRequestObj = inventoryRequestService.updateRequest(id, inventoryRequestDTO);

        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("inventory.item.update"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryRequestObj);
    }

    @PostMapping(value = URLConstant.INVENTORY_REQUEST)
    public ResponseEntity<Object> inventoryRequest(@Valid @RequestBody InventoryRequestDTO inventoryRequestDTO,
                                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }

        InventoryRequest inventoryRequestObj = inventoryRequestService.createRequest(inventoryRequestDTO);

        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("inventory.request.item"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryRequestObj);
    }

    @Operation(summary = "Register an user",
            description = "Request by user to register account i.e CNF.")
    @PostMapping(value = URLConstant.REGISTER, consumes = "multipart/form-data")
    public ResponseEntity<Object> registerUser(@Valid @ModelAttribute UserSignUpDTO userSignupDTO, BindingResult bindingResult, @RequestParam BigDecimal totalDeposite,
                                               @RequestParam PaymentMethod paymentMethod) {
        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }
        User registeredUser = userService.registerUser(userSignupDTO, totalDeposite, paymentMethod, userSignupDTO.getFile());
        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("user.registration.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE,
                userService.getRegistrationSuccessDetails(registeredUser, userSignupDTO.getPassword()));
    }


    @Operation(summary = "Get All inventory request by inventory request id")
    @GetMapping(value = URLConstant.GET_INVENTORY_REQUEST_BY_ID + "{inventoryRequestId}")
    public ResponseEntity<Object> getInventoryRequestById(@PathVariable("inventoryRequestId") UUID inventoryRequestId) {
        CNFInventoryRequestDTO inventoryListOfCNF = cnfService.getInventoryRequestsByInventoryRequestId(inventoryRequestId);
        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("inventory.request.found.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryListOfCNF);
    }

    @Operation(summary = "Get All inventory request to login cnf")
    @GetMapping(value = URLConstant.GET_INVENTORY_REQUEST_TO_LOGIN_CNF)
    public ResponseEntity<Object> getInventoryRequestTOLoginCNF() {
        List<InventoryItemRequestListOfCnfDTO> inventoryListOfCNFList = cnfService.getAllRequestOfCNF();
        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("inventory.request.found.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryListOfCNFList);
    }


    @Operation(summary = "Response by cnf of field agent request")
    @PutMapping(value = URLConstant.INVENTORY_ITEM_FIELDAGENT_CNF + "{inventoryRequestId}")
    public ResponseEntity<Object> approvedRequestByCNF(@PathVariable("inventoryRequestId") UUID inventoryRequestId, @RequestParam("inventoryStatus") InventoryStatus inventoryStatus) {
        fieldAgentService.approvedRequestByCNF(inventoryRequestId, inventoryStatus);
        return ResponseHandler.responseWithoutData(HttpStatus.OK, true,
                localService.getMessage("Response.request"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE);
    }


}