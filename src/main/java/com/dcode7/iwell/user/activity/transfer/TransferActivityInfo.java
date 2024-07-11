package com.dcode7.iwell.user.activity.transfer;

import java.math.BigDecimal;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.activity.ActivityInfo;
import com.dcode7.iwell.user.activity.ActivityType;

public class TransferActivityInfo extends ActivityInfo {

	private BigDecimal amount;

	private User recipientUser;

	public TransferActivityInfo(User user, BigDecimal amount, User recipientUser) {
		super(user, ActivityType.TRANSFER);
		this.amount = amount;
		this.recipientUser = recipientUser;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public User getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}

}
