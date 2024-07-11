package com.dcode7.iwell.user.activity.transfer;

import java.math.BigDecimal;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.activity.Activity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TransferActivity extends Activity {
	private BigDecimal amount;

	@ManyToOne
	@JoinColumn(name = "recipient_user_id")
	private User recipient;

	public TransferActivity(User user, BigDecimal amount, User recipient) {
		super(user);
		this.amount = amount;
		this.recipient = recipient;
	}

	public TransferActivity() {
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}
}
