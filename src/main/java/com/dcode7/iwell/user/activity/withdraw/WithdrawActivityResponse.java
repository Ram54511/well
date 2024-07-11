package com.dcode7.iwell.user.activity.withdraw;

import java.math.BigDecimal;
import java.util.Date;

import com.dcode7.iwell.user.activity.ActivityResponse;
import com.dcode7.iwell.user.activity.ActivityType;

public class WithdrawActivityResponse extends ActivityResponse {

	private BigDecimal amount;

	public WithdrawActivityResponse(Date timestamp, BigDecimal amount) {
		super(timestamp, ActivityType.WITHDRAW);
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
