package com.dcode7.iwell.user.login;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dcode7.iwell.user.User;

public interface LoginActivityRepository extends JpaRepository<LoginActivity, UUID> {

	Page<LoginActivity> findByUser(User user, Pageable pageable);
}