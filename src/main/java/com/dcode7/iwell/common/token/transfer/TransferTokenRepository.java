package com.dcode7.iwell.common.token.transfer;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.common.token.OtpType;

public interface TransferTokenRepository extends JpaRepository<TransferToken, UUID> {

	TransferToken findByEmail(String email);

	TransferToken findByEmailAndOtpType(String email, OtpType otpType);

}
