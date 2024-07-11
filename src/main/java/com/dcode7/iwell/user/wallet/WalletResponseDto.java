package com.dcode7.iwell.user.wallet;

import java.math.BigDecimal;

public class WalletResponseDto {

	private WalletType type;
	private BigDecimal balance;

	public WalletResponseDto() {
	}

	public WalletResponseDto(WalletType type, BigDecimal balance) {

		this.type = type;
		this.balance = balance;

	}

	public WalletType getType() {
		return type;
	}

	public void setType(WalletType type) {
		this.type = type;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
