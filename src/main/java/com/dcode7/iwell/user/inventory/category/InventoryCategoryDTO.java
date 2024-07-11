package com.dcode7.iwell.user.inventory.category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCategoryDTO {

	@NotNull(message = "category.name.required")
	private String name;

	@NotNull(message = "category.description.required")
	private String description;

}
