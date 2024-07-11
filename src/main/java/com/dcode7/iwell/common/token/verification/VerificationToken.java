package com.dcode7.iwell.common.token.verification;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "verification_token")
public class VerificationToken {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "otp", nullable = false)
	private String otp;

	@Column(name = "expiry_date", nullable = false)
	private LocalDateTime expiryDate;

	public VerificationToken() {
	}

	public VerificationToken(String email, String otp, LocalDateTime expiryDate) {
		this.email = email;
		this.otp = otp;
		this.expiryDate = expiryDate;
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

}
