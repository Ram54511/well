package com.dcode7.iwell.user.wallet.service;

import java.math.BigDecimal;
import java.util.List;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.transaction.enums.PaymentMethod;
import com.dcode7.iwell.user.wallet.Wallet;
import com.dcode7.iwell.user.wallet.WalletResponseDto;
import com.dcode7.iwell.user.wallet.WalletType;

public interface WalletService {

	Iterable<Wallet> findByUser(User user);

	void initializeWalletsForCNF(User user, BigDecimal totalDeposite,PaymentMethod paymentMethod);

	void initializeWallets(User user);

	Wallet addAmountToWallet(Wallet wallet, BigDecimal amount);

	Wallet deductAmountFromWallet(Wallet wallet, BigDecimal amount);

	BigDecimal getBalance(Wallet wallet);

	boolean isSameWallet(Wallet wallet1, Wallet wallet2);

	List<WalletResponseDto> getWalletsByUser(User user);

	Wallet findWalletByUserAndType(User user, WalletType walletType);

	void save(Wallet fundingWallet);

	Wallet getAdminWallet();
}