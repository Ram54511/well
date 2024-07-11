package com.dcode7.iwell.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dcode7.iwell.user.enums.UserRequestStatus;
import com.dcode7.iwell.user.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUserName(String userName);

	Optional<User> findByMobileNumber(String mobileNumber);

	User findByUserType(UserType userType);

	List<User> findByReferencedUserAndUserRequestStatus(User user, UserRequestStatus userRequestStatus);

//	@Query("SELECT u.referencedUser, COUNT(u) FROM User u GROUP BY u.referencedUserId")
	User findByReferencedUserId(User user);
}
