package com.dcode7.iwell.common.token;

import com.dcode7.iwell.common.token.transfer.TransferToken;
import com.dcode7.iwell.common.token.verification.VerificationToken;
import com.dcode7.iwell.common.token.withdrawl.WithdrawalToken;

public interface TokenService {

	public TransferToken getTransferTokenByEmail(String email);

	public WithdrawalToken getWithdrawalTokenByEmail(String email);

	public boolean isTransferTokenValid(String email, String token);

	public boolean isWithdrawalTokenValid(String email, String token);

	public VerificationToken getVerificationTokenByEmail(String email);

	public boolean isVerificationTokenValid(String email, String token);
}
