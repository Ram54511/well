package com.dcode7.iwell.user.activity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dcode7.iwell.user.User;

public interface UserActivityRepository extends JpaRepository<Activity, UUID> {

	List<Activity> findTopActivitiesByUserId(UUID userId, Pageable pageable);

	@Query("SELECT a FROM Activity a WHERE a.user = :user ORDER BY a.timestamp DESC LIMIT 1")
	Activity findLastActivityByUserId(@Param("user") User user);

}
