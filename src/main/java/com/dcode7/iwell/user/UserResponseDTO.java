package com.dcode7.iwell.user;

import com.dcode7.iwell.user.enums.Gender;

public class UserResponseDTO {

	private String userName;
	private String fullName;
	private String email;
	private Long countryCode;

	private Gender gender;
	private String profilePic;
	private Boolean isEmailVerified;
	private Boolean isMobileVerified;

	private String referralCode;

	public UserResponseDTO() {

	}

	public UserResponseDTO(User user, String referralCode) {
		this.userName = user.getUserName();
		this.fullName = user.getFullName();
		this.email = user.getEmail();
		this.countryCode = user.getCountryCode();
		this.gender = user.getGender();
		this.profilePic = user.getProfilePic();
		this.isEmailVerified = user.getIsEmailVerified();
		this.isMobileVerified = user.getIsMobileVerified();
		this.referralCode = referralCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Long countryCode) {
		this.countryCode = countryCode;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public Boolean getIsMobileVerified() {
		return isMobileVerified;
	}

	public void setIsMobileVerified(Boolean isMobileVerified) {
		this.isMobileVerified = isMobileVerified;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
}
