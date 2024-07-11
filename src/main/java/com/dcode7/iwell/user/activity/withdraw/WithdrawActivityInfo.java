package com.dcode7.iwell.user.activity.withdraw;

import java.math.BigDecimal;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.activity.ActivityInfo;
import com.dcode7.iwell.user.activity.ActivityType;

public class WithdrawActivityInfo extends ActivityInfo {

	private BigDecimal amount;

	public WithdrawActivityInfo(User user, BigDecimal amount) {
		super(user, ActivityType.WITHDRAW);
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
