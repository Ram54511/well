package com.dcode7.iwell.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class RequestRegistrationTokenRequestDTO {

	@Email(message = "{user.request.registration.token.email.invalid}")
	@NotNull(message = "{user.request.registration.token.email.required}")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
