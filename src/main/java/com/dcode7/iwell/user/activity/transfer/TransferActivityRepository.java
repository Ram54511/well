package com.dcode7.iwell.user.activity.transfer;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.user.User;

public interface TransferActivityRepository extends JpaRepository<TransferActivity, UUID> {

	Page<TransferActivity> findByUser(User user, Pageable pageable);

}