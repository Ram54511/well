package com.dcode7.iwell.user.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.dcode7.iwell.user.UserRepository;
import com.dcode7.iwell.user.transaction.Transaction;
import com.dcode7.iwell.user.transaction.enums.PaymentMethod;
import com.dcode7.iwell.user.transaction.enums.TransactionStatus;
import com.dcode7.iwell.user.transaction.enums.TransactionType;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.user.wallet.Wallet;
import com.dcode7.iwell.user.wallet.WalletRepository;
import com.dcode7.iwell.user.wallet.WalletResponseDto;
import com.dcode7.iwell.user.wallet.WalletType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

	private final WalletRepository walletRepository;

	private final UserRepository userRepository;

	@Override
	public Iterable<Wallet> findByUser(User user) {
		return walletRepository.findByUser(user);
	}

	private Wallet createWallet(User user, WalletType walletType) {
		Wallet wallet = new Wallet();
		wallet.setType(walletType);
		wallet.setUser(user);
		wallet.setBalance(new BigDecimal(0.0));
		return walletRepository.save(wallet);
	}

	private Wallet createWalletForCNF(User user, WalletType walletType, BigDecimal totalDeposite, PaymentMethod paymentMethod) {
		Wallet wallet = new Wallet();
		wallet.setType(walletType);
		wallet.setUser(user);
		wallet.setBalance(totalDeposite);

		User userObj = userRepository.findByUserType(UserType.ADMIN);
		Wallet walletObj = walletRepository.findByUserId(userObj.getId());
		Wallet walletDetail = walletRepository.save(wallet);
		Transaction transaction = new Transaction();
		transaction.setToWallet(walletObj);
		transaction.setFromWallet(walletDetail);
		transaction.setAmount(totalDeposite);
		transaction.setStatus(TransactionStatus.SUCCESS);
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setPaymentMethod(paymentMethod);
		return walletDetail;
	}

	/**
	 * Initialize the users' wallets when a user registers
	 */
	@Override
	public void initializeWallets(User user) {
		createWallet(user, WalletType.DEPOSIT);
	}

	public void initializeWalletsForCNF(User user, BigDecimal totalDeposite, PaymentMethod paymentMethod) {
		createWalletForCNF(user, WalletType.DEPOSIT,totalDeposite,paymentMethod);
	}

	@Override
	public BigDecimal getBalance(Wallet wallet) {
		return wallet.getBalance();
	}

	@Override
	public Wallet addAmountToWallet(Wallet wallet, BigDecimal amount) {
		wallet.setBalance(wallet.getBalance().add(amount));
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet deductAmountFromWallet(Wallet wallet, BigDecimal amount) {
		wallet.setBalance(wallet.getBalance().subtract(amount));
		return walletRepository.save(wallet);
	}

	@Override
	public boolean isSameWallet(Wallet wallet1, Wallet wallet2) {
		return wallet1.getId().equals(wallet2.getId());
	}

	@Override
	public List<WalletResponseDto> getWalletsByUser(User user) {
		return walletRepository.getWalletByUser(user).stream()
				.map(wallet -> new WalletResponseDto(wallet.getType(), wallet.getBalance()))
				.collect(Collectors.toList());
	}

	@Override
	public Wallet findWalletByUserAndType(User user, WalletType walletType) {
		return walletRepository.findWalletByUserAndType(user, walletType);
	}

	@Override
	public void save(Wallet fundingWallet) {
		walletRepository.save(fundingWallet);

	}

	@Override
	public Wallet getAdminWallet() {
		return walletRepository.findByUserUserType(UserType.ADMIN);

	}

}
