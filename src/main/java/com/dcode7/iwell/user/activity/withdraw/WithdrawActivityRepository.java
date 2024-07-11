package com.dcode7.iwell.user.activity.withdraw;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.user.User;

public interface WithdrawActivityRepository extends JpaRepository<WithdrawActivity, UUID> {

	Page<WithdrawActivity> findByUser(User user, Pageable pageable);

}