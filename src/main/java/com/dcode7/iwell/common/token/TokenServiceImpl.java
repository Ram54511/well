package com.dcode7.iwell.common.token;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.common.token.transfer.TransferToken;
import com.dcode7.iwell.common.token.transfer.TransferTokenRepository;
import com.dcode7.iwell.common.token.verification.VerificationToken;
import com.dcode7.iwell.common.token.verification.VerificationTokenRepository;
import com.dcode7.iwell.common.token.withdrawl.WithdrawalToken;
import com.dcode7.iwell.common.token.withdrawl.WithdrawalTokenRepository;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TransferTokenRepository transferTokenRepository;

	@Autowired
	private WithdrawalTokenRepository withdrawalTokenRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	public TransferToken getTransferTokenByEmail(String email) {
		return transferTokenRepository.findByEmail(email);
	}

	@Override
	public WithdrawalToken getWithdrawalTokenByEmail(String email) {
		return withdrawalTokenRepository.findByEmailAndOtpType(email, OtpType.WITHDRAWAL);
	}

	public boolean isTransferTokenValid(String email, String token) {
		TransferToken transferToken = transferTokenRepository.findByEmailAndOtpType(email, OtpType.FUND_TRANSFER);
		if (transferToken != null && transferToken.getOtp().equals(token)) {
			return transferToken.getExpiryDate().isAfter(LocalDateTime.now());
		}
		return false;
	}

	public boolean isWithdrawalTokenValid(String email, String token) {

		WithdrawalToken withdrawalToken = withdrawalTokenRepository.findByEmailAndOtpType(email, OtpType.WITHDRAWAL);

		return withdrawalToken != null && withdrawalToken.getOtp().equals(token)
				&& withdrawalToken.getExpiryDate().isAfter(LocalDateTime.now());

	}

	@Override
	public VerificationToken getVerificationTokenByEmail(String email) {
		return verificationTokenRepository.findByEmail(email);
	}

	@Override
	public boolean isVerificationTokenValid(String email, String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByEmail(email);
		if (verificationToken != null && verificationToken.getOtp().equals(token)) {
			return verificationToken.getExpiryDate().isAfter(LocalDateTime.now());
		}
		return false;
	}

}
