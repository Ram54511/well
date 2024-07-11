package com.dcode7.iwell.user.activity.transfer;

import java.math.BigDecimal;
import java.util.Date;

import com.dcode7.iwell.user.activity.ActivityResponse;
import com.dcode7.iwell.user.activity.ActivityType;

public class TransferActivityResponse extends ActivityResponse {

	private BigDecimal amount;

	private String recepientUsername;

	public TransferActivityResponse() {
	}

	public TransferActivityResponse(BigDecimal amount, String recepientUsername, Date timestamp) {
		super(timestamp, ActivityType.TRANSFER);
		this.amount = amount;
		this.recepientUsername = recepientUsername;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRecepientUsername() {
		return recepientUsername;
	}

	public void setRecepientUsername(String recepientUsername) {
		this.recepientUsername = recepientUsername;
	}

}
