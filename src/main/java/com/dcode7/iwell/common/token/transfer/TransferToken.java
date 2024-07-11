package com.dcode7.iwell.common.token.transfer;

import java.time.LocalDateTime;

import com.dcode7.iwell.common.token.OtpType;
import com.dcode7.iwell.common.token.verification.VerificationToken;
import com.dcode7.iwell.user.transaction.Transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;

@Entity
public class TransferToken extends VerificationToken {

	@OneToOne
	private Transaction transaction;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OtpType otpType;

	public TransferToken(String email, String otp, LocalDateTime expiryDate, Transaction transaction, OtpType otpType) {
		super(email, otp, expiryDate);
		this.transaction = transaction;
		this.otpType = otpType;
	}

	public TransferToken() {
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public OtpType getOtpType() {
		return otpType;
	}

	public void setOtpType(OtpType otpType) {
		this.otpType = otpType;
	}

}
