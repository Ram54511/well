package com.dcode7.iwell.user.activity.deposit;

import java.math.BigDecimal;
import java.util.Date;

import com.dcode7.iwell.user.activity.ActivityResponse;
import com.dcode7.iwell.user.activity.ActivityType;

public class DepositActivityResponse extends ActivityResponse {

	private BigDecimal amount;

	public DepositActivityResponse(Date timestamp, BigDecimal amount) {
		super(timestamp, ActivityType.DEPOSIT);
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
