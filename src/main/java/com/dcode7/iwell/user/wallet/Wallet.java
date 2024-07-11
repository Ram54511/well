package com.dcode7.iwell.user.wallet;

import java.math.BigDecimal;
import java.util.UUID;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.utils.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallet", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "type" }) })
public class Wallet extends BaseEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private WalletType type;

	@Min(0)
	@Column(nullable = false)
	private BigDecimal balance;

}
