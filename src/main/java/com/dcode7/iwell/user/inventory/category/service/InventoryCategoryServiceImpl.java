package com.dcode7.iwell.user.inventory.category.service;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
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
import com.dcode7.iwell.user.inventory.category.InventoryCategoryDTO;
import com.dcode7.iwell.user.inventory.category.InventoryCategoryRepository;
import com.dcode7.iwell.utils.GenericUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryCategoryServiceImpl implements InventoryCategoryService {

	private final InventoryCategoryRepository categoryRepository;

	private final LocalService localService;

	@Override
	/**
	 * Creates a new inventory category.
	 * 
	 * @param inventoryCategory The details of the new inventory category.
	 * @throws IllegalArgumentException If the provided inventoryCategory object is
	 *                                  null.
	 * @throws DataAccessException      If there is an error persisting the data to
	 *                                  the repository.
	 */
	public InventoryCategory createCategory(InventoryCategoryDTO inventoryCategoryDTO) {
		User user = GenericUtils.getLoggedInUser();
		validateUser(user);
		return saveCategory(inventoryCategoryDTO);

	}

	private InventoryCategory saveCategory(InventoryCategoryDTO inventoryCategoryDTO) {
		InventoryCategory category = new InventoryCategory();
		category.setName(inventoryCategoryDTO.getName());
		category.setDescription(inventoryCategoryDTO.getDescription());
		category.setDeleted(false);
		return categoryRepository.save(category);

	}

	@Override
	/**
	 * Updates an existing inventory category.
	 * 
	 * @param id                The unique identifier of the inventory category to
	 *                          update.
	 * @param inventoryCategory The updated inventory category details.
	 * @return
	 * @throws ResourceNotFoundException If the inventory category with the provided
	 *                                   ID is not found.
	 */
	@CacheEvict(value = "categories", key = "#id")
	public InventoryCategory updateCategory(UUID id, InventoryCategoryDTO inventoryCategoryDTO) {
		User user = GenericUtils.getLoggedInUser();
		validateUser(user);
		InventoryCategory inventoryCategoryObj = categoryRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("category.not.found"),
						HttpStatus.NOT_FOUND));
		inventoryCategoryObj.setName(inventoryCategoryDTO.getName());
		inventoryCategoryObj.setDescription(inventoryCategoryDTO.getDescription());
		inventoryCategoryObj.setDeleted(false);
		return categoryRepository.save(inventoryCategoryObj);
	}

	@Override
	/**
	 * Retrieves an inventory category by its unique identifier.
	 * 
	 * @param id The unique identifier of the inventory category to retrieve.
	 * @return The inventory category with the specified ID, or throws an exception
	 *         if not found.
	 * @throws ResourceNotFoundException If the inventory category with the provided
	 *                                   ID is not found.
	 */
	@Cacheable(value = "categoryById", key = "#id")
	public InventoryCategory getCategory(UUID id) {
		return categoryRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("category.not.found"),
						HttpStatus.NOT_FOUND));
	}

	@Override
	/**
	 * Retrieves all inventory categories.
	 * 
	 * @return A list containing all inventory categories from the repository.
	 */

	@Cacheable(value = "allCategories")
	public List<InventoryCategory> getAllCategory() {
		List<InventoryCategory> allCategories = categoryRepository.findAll();
		return allCategories;
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
				throw new UnauthorizedAccessException(localService.getMessage("user.not.access"),
						HttpStatus.BAD_REQUEST);
			}
			throw new UnauthorizedAccessException(localService.getMessage("user.login.user.not.registered"),
					HttpStatus.BAD_REQUEST);
		}
		return user;
	}

	@Override
	/**
	 * Performs a soft delete on an inventory category by setting its 'deleted' flag
	 * to true.
	 * 
	 * @param id The unique identifier of the inventory category to soft delete.
	 * @return True if the soft delete operation was successful, throws an exception
	 *         otherwise.
	 * @throws ResourceNotFoundException If the inventory category with the provided
	 *                                   ID is not found.
	 */
	@CacheEvict(value = "allCategories", allEntries = true)
	public Boolean softDeleteInventoryCategory(UUID id) {
		InventoryCategory inventoryCategory = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("category.not.found"),
						HttpStatus.NOT_FOUND));
		inventoryCategory.setDeleted(true);
		return true;
	}

}
