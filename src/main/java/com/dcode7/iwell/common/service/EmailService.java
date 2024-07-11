package com.dcode7.iwell.common.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.common.token.OtpType;
import com.dcode7.iwell.common.token.transfer.TransferToken;
import com.dcode7.iwell.common.token.transfer.TransferTokenRepository;
import com.dcode7.iwell.common.token.verification.VerificationToken;
import com.dcode7.iwell.common.token.verification.VerificationTokenRepository;
import com.dcode7.iwell.common.token.withdrawl.WithdrawalToken;
import com.dcode7.iwell.common.token.withdrawl.WithdrawalTokenRepository;
import com.dcode7.iwell.user.transaction.Transaction;
import com.dcode7.iwell.utils.TokenGenerator;

@Service
public class EmailService {

	private static final int TOKEN_VALIDITY_MINUTES = 15;
	private static final String VERIFICATION_SUBJECT = "Your Verification Token";
	private static final String VERIFICATION_MESSAGE = "Please use the following token to verify your email: %s";

	private static final int TRANSFER_OTP_VALIDITY_MINUTES = 10;
	private static final String FUND_TRANSFER_OTP_EMAIL_SUBJECT = "Your OTP for Fund Transfer";
	private static final String FUND_TRANSFER_OTP_EMAIL_MESSAGE_FORMAT = "Please use the following OTP to authenticate Fund Transfer: %s";

	private static final int WITHDRAWAL_OTP_VALIDITY_MINUTES = 10;
	private static final String WITHDRAWAL_OTP_EMAIL_SUBJECT = "Your OTP for Withdrawal";
	private static final String WITHDRAWAL_OTP_EMAIL_MESSAGE_FORMAT = "Please use the following OTP to authenticate Withdrawal: %s";

	@Value("${spring.mail.username}")
	private String fromAddress;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private TransferTokenRepository transferTokenRepository;

	@Autowired
	private WithdrawalTokenRepository withdrawalTokenRepository;

	public VerificationToken generateAndSaveVerificationTokenForSignUp(String email) {

		LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES);
		VerificationToken verificationToken = tokenRepository.findByEmail(email);
		String token = TokenGenerator.generateToken(6);

		if (Objects.isNull(verificationToken)) {
			verificationToken = new VerificationToken(email, token, expiryDate);
		} else {
			verificationToken.setExpiryDate(expiryDate);
			verificationToken.setOtp(token);
		}

		tokenRepository.save(verificationToken); // Uncommented as it's supposed to save the token
		sendVerificationEmail(email, token);

		return verificationToken;
	}

	private void sendVerificationEmail(String email, String token) {
		sendSimpleMessage(email, VERIFICATION_SUBJECT, String.format(VERIFICATION_MESSAGE, token));
	}

	private void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromAddress);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

	public TransferToken generateAndSendOtpForTransfer(String email, Transaction transaction, OtpType otpType) {
		LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(TRANSFER_OTP_VALIDITY_MINUTES);
		TransferToken transferToken = transferTokenRepository.findByEmailAndOtpType(email, otpType);

		String otp = TokenGenerator.generateNumericOtp(6);

		if (Objects.isNull(transferToken)) {
			transferToken = new TransferToken(email, otp, expiryDate, transaction, otpType);
		} else {
			transferToken.setExpiryDate(expiryDate);
			transferToken.setOtp(otp);
			transferToken.setTransaction(transaction);
		}

		transferTokenRepository.save(transferToken);

		sendTransferTokenToEmail(transferToken);

		return transferToken;
	}

	public WithdrawalToken generateAndSendOtpForWithdrawal(String email, Transaction transaction) {
		LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(WITHDRAWAL_OTP_VALIDITY_MINUTES);
		WithdrawalToken withdrawalToken = withdrawalTokenRepository.findByEmailAndOtpType(email, OtpType.WITHDRAWAL);

		String otp = TokenGenerator.generateNumericOtp(6);

		if (Objects.isNull(withdrawalToken)) {
			withdrawalToken = new WithdrawalToken(email, otp, expiryDate, transaction);
		} else {
			withdrawalToken.setExpiryDate(expiryDate);
			withdrawalToken.setOtp(otp);
			withdrawalToken.setTransaction(transaction);
		}

		withdrawalTokenRepository.save(withdrawalToken);

		sendWithdrawalTokenToEmail(withdrawalToken);

		return withdrawalToken;

	}

	public void sendOtpEmail(String email, String otp) {
		sendSimpleMessage(email, FUND_TRANSFER_OTP_EMAIL_SUBJECT,
				String.format(FUND_TRANSFER_OTP_EMAIL_MESSAGE_FORMAT, otp));
	}

	private void sendTransferTokenToEmail(TransferToken transferToken) {
		String subject = FUND_TRANSFER_OTP_EMAIL_SUBJECT;
		String message = String.format(FUND_TRANSFER_OTP_EMAIL_MESSAGE_FORMAT, transferToken.getOtp());

		sendSimpleMessage(transferToken.getEmail(), subject, message);
	}

	private void sendWithdrawalTokenToEmail(WithdrawalToken withdrawalToken) {
		String subject = WITHDRAWAL_OTP_EMAIL_SUBJECT;
		String message = String.format(WITHDRAWAL_OTP_EMAIL_MESSAGE_FORMAT, withdrawalToken.getOtp());

		sendSimpleMessage(withdrawalToken.getEmail(), subject, message);
	}
}
