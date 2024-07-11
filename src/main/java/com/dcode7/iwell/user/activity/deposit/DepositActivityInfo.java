package com.dcode7.iwell.user.activity.deposit;

import java.math.BigDecimal;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.activity.ActivityInfo;
import com.dcode7.iwell.user.activity.ActivityType;

public class DepositActivityInfo extends ActivityInfo {

	private BigDecimal amount;

	public DepositActivityInfo(User user, BigDecimal amount) {
		super(user, ActivityType.DEPOSIT);
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
