package com.dcode7.iwell.user.transaction;

import java.math.BigDecimal;
import java.util.UUID;

import com.dcode7.iwell.user.transaction.enums.PaymentMethod;
import com.dcode7.iwell.user.transaction.enums.TransactionStatus;
import com.dcode7.iwell.user.transaction.enums.TransactionType;
import com.dcode7.iwell.user.wallet.Wallet;
import com.dcode7.iwell.utils.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private BigDecimal amount;

	@ManyToOne
	private Wallet fromWallet;

	@ManyToOne
	private Wallet toWallet;

	private UUID of;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionStatus status = TransactionStatus.PENDING;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionType type;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

}
