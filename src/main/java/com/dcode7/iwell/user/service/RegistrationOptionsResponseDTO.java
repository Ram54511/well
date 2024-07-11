package com.dcode7.iwell.user.service;

import java.util.ArrayList;

import com.dcode7.iwell.user.enums.UserType;

public class RegistrationOptionsResponseDTO {

	private ArrayList<UserType> userTypes;

	public RegistrationOptionsResponseDTO() {

	}

	public RegistrationOptionsResponseDTO(ArrayList<UserType> userTypes) {
		this.userTypes = userTypes;
	}

	public ArrayList<UserType> getUserTypes() {
		return userTypes;
	}

}
