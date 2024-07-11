package com.dcode7.iwell.user.inventory.category;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.user.inventory.category.service.InventoryCategoryService;
import com.dcode7.iwell.utils.ErrorCollectionUtil;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Inventory category Managment")
public class InventoryCategoryController {
	private final InventoryCategoryService inventoryCategoryService;
	private final LocalService localService;

	@PostMapping(value = URLConstant.ADMIN_ADD_INVENTORY_CATEGORIES)
	public ResponseEntity<Object> createCategory(@Valid @RequestBody InventoryCategoryDTO inventoryCategoryDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
					ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
					ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
		}
		InventoryCategory inventoryCategory = inventoryCategoryService.createCategory(inventoryCategoryDTO);
		return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("category.created"),
				ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryCategory);
	}

	@PutMapping(value = URLConstant.ADMIN_UPDATE_INVENTORY_CATEGORIES + "{id}")
	public ResponseEntity<Object> updateCategory(@PathVariable("id") UUID id,
			@RequestBody InventoryCategoryDTO inventoryCategoryDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
					ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
					ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
		}
		InventoryCategory inventoryCategory = inventoryCategoryService.updateCategory(id, inventoryCategoryDTO);
		return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("category.updated"),
				ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryCategory);
	}

	@GetMapping(value = URLConstant.ADMIN_GET_INVENTORY_CATEGORIES + "{id}")
	public ResponseEntity<Object> getInventoryCategoryById(@PathVariable("id") UUID id) {
		InventoryCategory inventoryCategory = inventoryCategoryService.getCategory(id);
		return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("category.found.success"),
				ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryCategory);
	}

	@GetMapping(value = URLConstant.GET_ALL_INVENTORY_CATEGORIES)
	public ResponseEntity<Object> getAllInventoryCategory() {
		List<InventoryCategory> inventoryCategory = inventoryCategoryService.getAllCategory();
		return ResponseHandler.responseWithData(HttpStatus.OK, true, localService.getMessage("category.found.success"),
				ErrorCode.OK, ResponseCode.ACKNOWLEDGE, inventoryCategory);
	}

	@DeleteMapping(value = URLConstant.SOFT_DELETE_INVENTORY_CATEGORIES + "{id}")
	public ResponseEntity<Object> softDeleteInventoryCategory(@PathVariable("id") UUID id) {
		Boolean inventoryCategory = inventoryCategoryService.softDeleteInventoryCategory(id);
		return ResponseHandler.responseWithData(HttpStatus.OK, true,
				localService.getMessage("category.deleted.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE,
				inventoryCategory);
	}

}
