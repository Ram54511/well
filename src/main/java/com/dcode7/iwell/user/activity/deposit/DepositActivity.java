package com.dcode7.iwell.user.activity.deposit;

import java.math.BigDecimal;

import com.dcode7.iwell.user.activity.Activity;

import jakarta.persistence.Entity;

@Entity
public class DepositActivity extends Activity {
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
