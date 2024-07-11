package com.dcode7.iwell.common.token.verification;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

	VerificationToken findByEmail(String email);

}
