package com.dcode7.iwell.user.transaction;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.user.transaction.enums.TransactionStatus;
import com.dcode7.iwell.user.wallet.Wallet;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

	Iterable<Transaction> findByFromWallet(Wallet fromWallet);

	Iterable<Transaction> findByToWallet(Wallet toWallet);

	Iterable<Transaction> findByStatus(TransactionStatus status);

}
