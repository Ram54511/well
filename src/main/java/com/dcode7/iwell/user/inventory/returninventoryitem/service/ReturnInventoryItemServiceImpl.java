package com.dcode7.iwell.user.inventory.returninventoryitem.service;

import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.exception.ResourceNotFoundException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetail;
import com.dcode7.iwell.user.inventory.returninventoryitem.*;
import com.dcode7.iwell.utils.GenericUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReturnInventoryItemServiceImpl implements ReturnInventoryItemService {
    private final ReturnInventoryItemRepository returnInventoryItemRepository;
    private final InventoryRequestRepository inventoryRequestRepository;
    private final LocalService localService;

    /**
     * Handles the process of returning an inventory item based on the provided DTO and return type.
     *
     * @param returnInventoryItemDTO The DTO containing information for returning the item.
     * @param returnType             The type of return.
     * @return The created ReturnInventoryItem.
     */
    @Override
    public ReturnInventoryItem returnInventoryItem(ReturnInventoryItemDTO returnInventoryItemDTO, ReturnType returnType) {
        User user = getLoggedInUser();
        InventoryRequest inventoryRequest =  getInventoryRequestById(returnInventoryItemDTO.getInventoryRequestId());
        boolean isRefundableAndExchangeableItemExists = false;
        for (InventoryRequestDetail details : inventoryRequest.getInventoryRequestDetail()) {
            if (details.getRequestedItem().getIsRefundable() && details.getRequestedItem().getIsExchangeable()) {
                isRefundableAndExchangeableItemExists = true;
                break;
            }
        }
        if (isRefundableAndExchangeableItemExists) {
            if (user.getUserType() == UserType.CNF || user.getUserType() == UserType.FIELDAGENT) {
                ReturnInventoryItem returnInventoryItem = createReturnInventoryItem(returnInventoryItemDTO, returnType, user);
                return saveReturnInventoryItem(returnInventoryItem);
            } else {
                throw new CustomException(localService.getMessage("user.not.allowed"), HttpStatus.NOT_FOUND);
            }
        } else {
            throw new CustomException(localService.getMessage("no.refundable.exchangeable.item"), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    @CacheEvict(value = "returnInventoryItems", key = "#returnInventoryItemId")
    public ReturnInventoryItem responsereturnInventoryItemFromAdmin(ReturnStatus returnStatus, UUID returnInventoryItemId) {
        ReturnInventoryItem returnInventoryItem = getReturnInventoryRequestById(returnInventoryItemId);
        if(returnInventoryItem.getReturnStatus() == ReturnStatus.ACCEPTED){
            throw new CustomException(localService.getMessage("already.accepted"), HttpStatus.BAD_REQUEST);
        }else{
            returnInventoryItem.setReturnStatus(returnStatus);
        }
        return returnInventoryItemRepository.save(returnInventoryItem);
    }

    /**
     * Saves a ReturnInventoryItem object to the repository.
     *
     * @param returnInventoryItem The ReturnInventoryItem to save.
     * @return The saved ReturnInventoryItem.
     */
    public ReturnInventoryItem saveReturnInventoryItem(ReturnInventoryItem returnInventoryItem) {
        return returnInventoryItemRepository.save(returnInventoryItem);
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The currently logged-in user.
     */
    public User getLoggedInUser() {
        return GenericUtils.getLoggedInUser();
    }

    /**
     * Retrieves an inventory request by its ID from the repository.
     *
     * @param inventoryRequestId The unique identifier of the inventory request.
     * @return The retrieved inventory request.
     * @throws ResourceNotFoundException If the inventory request with the specified
     *                                   ID is not found.
     */
    public InventoryRequest getInventoryRequestById(UUID inventoryRequestId) {
        return inventoryRequestRepository.findById(inventoryRequestId)
                .orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("request.not.found"), HttpStatus.NOT_FOUND));
    }

    /**
     * Creates a new ReturnInventoryItem object based on the provided DTO, return type, and user.
     *
     * @param returnInventoryItemDTO The DTO containing information for creating the ReturnInventoryItem.
     * @param returnType             The type of return.
     * @param user                   The user initiating the return.
     * @return The created ReturnInventoryItem.
     */
    public ReturnInventoryItem createReturnInventoryItem(ReturnInventoryItemDTO returnInventoryItemDTO, ReturnType returnType, User user) {
        ReturnInventoryItem returnInventoryItem = new ReturnInventoryItem();
        returnInventoryItem.setInventoryRequest(getInventoryRequestById(returnInventoryItemDTO.getInventoryRequestId()));
        returnInventoryItem.setReturnDate(new Date());
        returnInventoryItem.setReturnStatus(ReturnStatus.PENDING);
        returnInventoryItem.setRestockingFeePercentage(0.00);
        returnInventoryItem.setRestockingFee(true);
        returnInventoryItem.setReturnType(returnType);
        returnInventoryItem.setUser(user);
        returnInventoryItem.setFullDescription(returnInventoryItemDTO.getFullDescription());
        return returnInventoryItem;
    }


    /**
     * Retrieves an return inventory request by its ID from the repository.
     *
     * @param returnInventoryItemId The unique identifier of the inventory request.
     * @return The retrieved return inventory request.
     * @throws ResourceNotFoundException If the inventory request with the specified
     *                                   ID is not found.
     */
    public ReturnInventoryItem getReturnInventoryRequestById(UUID returnInventoryItemId) {
        return returnInventoryItemRepository.findById(returnInventoryItemId)
                .orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("request.not.found"), HttpStatus.NOT_FOUND));
    }
}
