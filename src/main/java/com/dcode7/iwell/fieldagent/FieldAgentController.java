package com.dcode7.iwell.fieldagent;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.fieldagent.fieldagentservice.FieldAgentService;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Field Agent Management")
public class FieldAgentController {

    private final FieldAgentService fieldAgentService;

    private final LocalService localService;

    @Operation(summary = "Get All inventory item of login field agent's cnf if cnf isn't assign then admin")
    @GetMapping(value = URLConstant.INVENTORY_ITEM_FIELDAGENT_CNF)
    public ResponseEntity<Object> getAllFieldAgentOfLoginCnf() {
        List<CNFInventoryItemDTO> inventoryRequest = fieldAgentService.getInventoryItemOfCnf();
        return ResponseHandler.responseWithData(HttpStatus.OK, true,
                localService.getMessage("inventory.request.found.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE,inventoryRequest);
    }
    @Operation(summary = "Request inventory item to cnf from fieldagent")
    @PostMapping(value = URLConstant.FIELDAGENT_INVENTORY_REQUEST)
    public ResponseEntity<Object> cnfInventoryRequest(@Valid @RequestBody InventoryRequestDTO inventoryRequestDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }
        InventoryRequest inventoryRequestObj = fieldAgentService.createRequest(inventoryRequestDTO);
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("inventory.request.item"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryRequestObj);
    }
}
