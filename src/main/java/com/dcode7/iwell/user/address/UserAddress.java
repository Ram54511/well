package com.dcode7.iwell.user.address;

import java.util.UUID;

import com.dcode7.iwell.user.User;

import com.dcode7.iwell.user.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {

	@Id
	@GeneratedValue
	private UUID id;

	@OneToOne
	private User user;

	private String pincode;

	private String state;

	private String district;

	private String mailingAddress;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType userType;

}
