package com.dcode7.iwell.security.login;

public class AuthenticationResponseDTO {
	private String accessToken;
	private String refreshToken;
	private String fullName;

	public AuthenticationResponseDTO(String accessToken, String refreshToken, String fullName) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.fullName = fullName;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
