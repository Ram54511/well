package com.dcode7.iwell.user;

public class UserSignupResponseDTO {
	
	private String fullName;
	
	private String username;
	
	private String password;
	
	private String email;

	public UserSignupResponseDTO(String fullName, String username, String password, String email) {
		this.fullName = fullName;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
