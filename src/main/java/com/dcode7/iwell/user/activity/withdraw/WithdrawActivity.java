package com.dcode7.iwell.user.activity.withdraw;

import java.math.BigDecimal;

import com.dcode7.iwell.user.activity.Activity;

import jakarta.persistence.Entity;

@Entity
public class WithdrawActivity extends Activity {
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
