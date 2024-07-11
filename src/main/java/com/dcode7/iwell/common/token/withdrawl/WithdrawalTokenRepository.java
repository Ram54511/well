package com.dcode7.iwell.common.token.withdrawl;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.common.token.OtpType;

public interface WithdrawalTokenRepository extends JpaRepository<WithdrawalToken, UUID> {

	WithdrawalToken findByEmail(String email);

	WithdrawalToken findByEmailAndOtpType(String email, OtpType otpType);

}
