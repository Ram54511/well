package com.dcode7.iwell.user.wallet;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.enums.UserType;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

	Iterable<Wallet> findByUser(User user);

	List<Wallet> getWalletByUser(User user);

	Wallet findWalletByUserAndType(User user, WalletType walletType);

	Wallet findByUserUserType(UserType userType);

	Wallet findByUserId(UUID userId);

	Wallet findByUserIdAndType(UUID userId, WalletType walletType);

}