package com.dcode7.iwell.common.token.withdrawl;

import java.time.LocalDateTime;

import com.dcode7.iwell.common.token.OtpType;
import com.dcode7.iwell.common.token.verification.VerificationToken;
import com.dcode7.iwell.user.transaction.Transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;

@Entity
public class WithdrawalToken extends VerificationToken {

	@OneToOne(fetch = FetchType.LAZY)
	private Transaction transaction;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OtpType otpType = OtpType.WITHDRAWAL;

	public WithdrawalToken(String email, String otp, LocalDateTime expiryDate, Transaction transaction) {
		super(email, otp, expiryDate);
		this.transaction = transaction;
	}

	public WithdrawalToken() {
	}

	public OtpType getOtpType() {
		return otpType;
	}

	public void setOtpType(OtpType otpType) {
		this.otpType = otpType;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
