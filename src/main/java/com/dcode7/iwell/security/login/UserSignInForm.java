package com.dcode7.iwell.security.login;

import jakarta.validation.constraints.NotBlank;

public class UserSignInForm {
	
	@NotBlank(message = "{user.login.email.or.username.required}")
	private String emailOrUsername;
	
	@NotBlank(message = "{user.login.password.required}")
	private String password;

	public String getEmailOrUsername() {
		return emailOrUsername;
	}

	public void setEmailOrUsername(String emailOrUsername) {
		this.emailOrUsername = emailOrUsername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
