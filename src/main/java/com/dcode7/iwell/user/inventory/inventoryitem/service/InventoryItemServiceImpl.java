package com.dcode7.iwell.user.inventory.inventoryitem.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.dcode7.iwell.user.invoice.taxrates.TaxRate;
import com.dcode7.iwell.user.invoice.taxrates.TaxRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.common.exception.ResourceNotFoundException;
import com.dcode7.iwell.common.exception.UnauthorizedAccessException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.user.inventory.category.InventoryCategory;
import com.dcode7.iwell.user.inventory.category.InventoryCategoryRepository;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemDto;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemRepository;
import com.dcode7.iwell.utils.GenericUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

    private final LocalService localService;

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryCategoryRepository inventoryCategoryRepository;

    private final TaxRepository taxRepository;

    @Override
    /**
     * Creates a new inventory item.
     *
     * @param inventoryItem The details of the new inventory item.
     * @throws UnauthorizedAccessException if the user is not admin.
     */ public InventoryItem createInventoryItem(InventoryItemDto inventoryItemDto) {
        User user = GenericUtils.getLoggedInUser();
        validateUser(user);
        return create(inventoryItemDto);

    }

    private InventoryItem create(InventoryItemDto inventoryItemDto) {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setStorageCondition(inventoryItemDto.getStorageCondition());
        inventoryItem.setBatchNumber(inventoryItemDto.getBatchNumber());
        inventoryItem.setDescrition(inventoryItemDto.getDescription());
        inventoryItem.setExpiryDate(inventoryItemDto.getExpiryDate());
        inventoryItem.setHSNCode(inventoryItemDto.getHSNCode());
        UUID categoryId = inventoryItemDto.getInventoryCategoryId();

        if (categoryId != null) {
            InventoryCategory inventoryCategory = inventoryCategoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("category.not.found"), HttpStatus.NOT_FOUND));
            inventoryItem.setInventoryCategory(inventoryCategory);
        } else {
            throw new ResourceNotFoundException(localService.getMessage("category.not.found"), HttpStatus.NOT_FOUND);
        }
        UUID taxId = inventoryItemDto.getTaxId();
        TaxRate taxRate = taxRepository.findById(taxId).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("tax.not.found"), HttpStatus.NOT_FOUND));
        inventoryItem.setTaxRate(taxRate);
        inventoryItem.setManufacturer(inventoryItemDto.getManufacturer());
        inventoryItem.setDeleted(false);
        inventoryItem.setName(inventoryItemDto.getName());
        inventoryItem.setNoOfStock(inventoryItemDto.getNoOfStock());
        inventoryItem.setIsRefundable(inventoryItemDto.getIsRefundable());
        inventoryItem.setIsExchangeable(inventoryItemDto.getIsExchangeable());
        inventoryItem.setRegisterDate(inventoryItemDto.getRegisterDate());
        BigDecimal taxRatePercentage = taxRate.getRate();
        BigDecimal taxRateValue = taxRatePercentage.divide(BigDecimal.valueOf(100));
        BigDecimal sellingPrice = inventoryItemDto.getSellingPrice();
        BigDecimal taxAmount = sellingPrice.multiply(taxRateValue);
        BigDecimal totalAmountIncludingTax = sellingPrice.add(taxAmount);
        inventoryItem.setSellingPrice(totalAmountIncludingTax);
        inventoryItem.setStockKeepingUnit(inventoryItemDto.getStockKeepingUnit());
        inventoryItem.setUnitCost(inventoryItemDto.getUnitCost());
        return inventoryItemRepository.save(inventoryItem);
    }

    @Override
    /**
     * update existing inventory item.
     *
     * @param inventoryItem The details of the existing inventory item.
     * @throws DataAccessException      If there is an error persisting the data to
     *                                  the repository.
     * @throws IllegalArgumentException If the provided inventoryItem object is
     *                                  null.
     */
    @CachePut(value = "inventoryItems", key = "#id")
    @Transactional
    public InventoryItem updateInventoryItem(UUID id, InventoryItemDto inventoryItemDTO) {
        InventoryItem existingItem = fetchInventoryItemById(id);
        return updateItemInfo(existingItem, inventoryItemDTO);
    }

    // find inventory item by id
    @Cacheable(value = "inventoryItems", key = "#id")
    @Transactional
    private InventoryItem fetchInventoryItemById(UUID id) {
        return inventoryItemRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("inventory.item.not.found", HttpStatus.NOT_FOUND));
    }

    // update inventory item
    private InventoryItem updateItemInfo(InventoryItem existingItem, InventoryItemDto inventoryItemDTO) {
        existingItem.setName(inventoryItemDTO.getName());
        existingItem.setStorageCondition(inventoryItemDTO.getStorageCondition());
        existingItem.setBatchNumber(inventoryItemDTO.getBatchNumber());
        existingItem.setNoOfStock(inventoryItemDTO.getNoOfStock());
        existingItem.setStockKeepingUnit(inventoryItemDTO.getStockKeepingUnit());
        existingItem.setDescrition(inventoryItemDTO.getDescription());
        existingItem.setHSNCode(inventoryItemDTO.getHSNCode());
        existingItem.setExpiryDate(inventoryItemDTO.getExpiryDate());
        existingItem.setManufacturer(inventoryItemDTO.getManufacturer());
        existingItem.setRegisterDate(inventoryItemDTO.getRegisterDate());
        existingItem.setIsRefundable(inventoryItemDTO.getIsRefundable());
        existingItem.setIsExchangeable(inventoryItemDTO.getIsExchangeable());
        existingItem.setUnitCost(inventoryItemDTO.getUnitCost());
        existingItem.setSellingPrice(inventoryItemDTO.getSellingPrice());
        UUID categoryId = inventoryItemDTO.getInventoryCategoryId();
        if (categoryId != null) {
            InventoryCategory inventoryCategory = inventoryCategoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("category.not.found"), HttpStatus.NOT_FOUND));
            existingItem.setInventoryCategory(inventoryCategory);
        } else {
            throw new ResourceNotFoundException(localService.getMessage("category.not.found"), HttpStatus.NOT_FOUND);
        }
        return inventoryItemRepository.save(existingItem);
    }

    @Override
    /**
     * Retrieves an inventory item by its unique identifier.
     *
     * @param id The unique identifier of the inventory item to retrieve.
     * @return The inventory item with the specified ID, or throws an exception if
     *         not found.
     * @throws ResourceNotFoundException If the inventory item with the provided ID
     *                                   is not found.
     */
    @Cacheable(value = "inventoryItems", key = "#id")
    public InventoryItem findById(UUID id) {
        return inventoryItemRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("inventory.item.not.found"), HttpStatus.NOT_FOUND));
    }

    /**
     * Validates the provided user object for authorization.
     *
     * @param user The user object to validate.
     * @throws UnauthorizedAccessException If the user is null, not logged in, or
     *                                     does not have sufficient privileges.
     */
    private User validateUser(User user) {
        if (user == null) {
            if (user.getUserType() != UserType.ADMIN) {
                throw new UnauthorizedAccessException(localService.getMessage("user.not.access"), HttpStatus.BAD_REQUEST);
            }
            throw new UnauthorizedAccessException(localService.getMessage("user.login.user.not.registered"), HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    /**
     * Retrieves all inventory items from the repository where deleted is false.
     *
     * @return A list containing all inventory items.
     */

    @Override
    @Cacheable("inventoryItems")
    public List<InventoryItem> getAllInventoryItem() {
        List<InventoryItem> allInventoryItems = inventoryItemRepository.findAllByDeletedFalse();

        Map<String, InventoryItem> earliestExpiryItemsMap = allInventoryItems.stream().collect(Collectors.toMap(InventoryItem::getName, item -> item, (existing, replacement) -> {
            if (existing.getExpiryDate().compareTo(replacement.getExpiryDate()) < 0) {
                return existing;
            } else {
                return replacement;
            }
        }));
        return earliestExpiryItemsMap.values().stream().sorted(Comparator.comparing(InventoryItem::getExpiryDate).thenComparing(InventoryItem::getRegisterDate)).collect(Collectors.toList());
    }

    /**
     * Performs a soft delete on an inventory item by setting its 'deleted' flag to
     * true.
     *
     * @param id The unique identifier of the inventory item to soft delete.
     * @return True if the soft delete operation was successful, throws an exception
     * otherwise.
     * @throws ResourceNotFoundException If the inventory item with the provided ID
     *                                   is not found.
     */
    @Override
    @CacheEvict(value = "inventoryItems", key = "#id")
    public Boolean softdeleteInventoryItem(UUID id) {
        InventoryItem inventoryItem = inventoryItemRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("inventory.item.not.found", HttpStatus.NOT_FOUND));
        inventoryItem.setDeleted(true);
        inventoryItemRepository.save(inventoryItem);
        return true;
    }

}
