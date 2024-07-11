package com.dcode7.iwell.user.inventory.category;

import java.util.UUID;

import com.dcode7.iwell.utils.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InventoryCategory extends BaseEntity {

	@Id
	@GeneratedValue
	private UUID id;
	private String name;
	private String description;
	private Boolean deleted;
}
