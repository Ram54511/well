package com.dcode7.iwell.user.activity.deposit;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.user.User;

public interface DepositActivityRepository extends JpaRepository<DepositActivity, UUID> {

	Page<DepositActivity> findByUser(User user, Pageable pageable);

}