package com.dcode7.iwell.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dcode7.iwell.common.pincode.Pincode;
import com.dcode7.iwell.user.address.UserAddress;
import com.dcode7.iwell.user.enums.UserRequestStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dcode7.iwell.user.enums.Gender;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.utils.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class User extends BaseEntity implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private UUID id;

	@Column(unique = true)
	private String userName;

	@Column(nullable = false)
	private String fullName;

	private String password;

	private String referralIBUsername;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String mobileNumber;

	private Long countryCode;

	private Date dob;

	private Date doj;

	private Boolean isActive = false;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String profilePic;

	private Boolean isEmailVerified = false;

	private Boolean isMobileVerified = false;

	private Boolean isUserBlocked = false;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType userType;


	private Date deletedOn;

	private Boolean isDeleted = false;

	private Boolean isFirstLogin = true;

	private Boolean isRegistered = false;

	@Column(nullable = false)
	private Boolean isActivityLogSaved = true;

	@ManyToOne
	private User referencedUser;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRequestStatus userRequestStatus;

	public UserRequestStatus getUserRequestStatus() {
		return userRequestStatus;
	}

	public void setUserRequestStatus(UserRequestStatus userRequestStatus) {
		this.userRequestStatus = userRequestStatus;
	}

	public User getReferencedUser() {
		return referencedUser;
	}

	public void setReferencedUser(User referencedUser) {
		this.referencedUser = referencedUser;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReferralIBUsername() {
		return referralIBUsername;
	}

	public void setReferralIBUsername(String referralIBUsername) {
		this.referralIBUsername = referralIBUsername;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Long countryCode) {
		this.countryCode = countryCode;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getDoj() {
		return doj;
	}

	public void setDoj(Date doj) {
		this.doj = doj;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Boolean getIsUserBlocked() {
		return isUserBlocked;
	}

	public void setIsUserBlocked(Boolean isUserBlocked) {
		this.isUserBlocked = isUserBlocked;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public Boolean getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorites = new ArrayList<SimpleGrantedAuthority>();
		authorites.add(new SimpleGrantedAuthority("ROLE_" + userType.toString()));
		return authorites;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Boolean getIsActivityLogSaved() {
		return isActivityLogSaved;
	}

	public void setIsActivityLogSaved(Boolean isActivityLogSaved) {
		this.isActivityLogSaved = isActivityLogSaved;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}


}