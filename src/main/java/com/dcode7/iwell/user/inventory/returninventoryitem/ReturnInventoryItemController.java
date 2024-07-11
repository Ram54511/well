package com.dcode7.iwell.user.inventory.returninventoryitem;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.user.inventory.returninventoryitem.service.ReturnInventoryItemService;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Return Inventory item Managment")
public class ReturnInventoryItemController {
    private final ReturnInventoryItemService returnInventoryItemService;
    private final LocalService localService;

    @PostMapping(value = URLConstant.CREATE_INVENTORY_RETURN_REQUEST)
    public ResponseEntity<Object> createReturnRequest(@Valid @RequestBody ReturnInventoryItemDTO returnInventoryItemDTO,
                                                      @RequestParam("ReturnType") ReturnType returnType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
                    ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
                    ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
        }
        ReturnInventoryItem returnInventoryItem = returnInventoryItemService.returnInventoryItem(returnInventoryItemDTO, returnType);
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("return.inventory.item.created"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, returnInventoryItem);
    }

    @PutMapping(value = URLConstant.RESPONSE_INVENTORY_RETURN_REQUEST + "{returnRequestId}")
    public ResponseEntity<Object> responseReturnInventoryFromAdmin(@PathVariable("returnRequestId") UUID returnRequestId,
                                                 @RequestParam("returnStatus") ReturnStatus returnStatus) {
        ReturnInventoryItem returnInventoryItem = returnInventoryItemService.responsereturnInventoryItemFromAdmin(returnStatus,returnRequestId);
        return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("return.status.updated"),
                ErrorCode.OK, ResponseCode.ACKNOWLEDGE, returnInventoryItem);
    }
}
