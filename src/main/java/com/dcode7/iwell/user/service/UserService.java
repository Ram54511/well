package com.dcode7.iwell.user.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.dcode7.iwell.user.FieldAgentSignUpDTO;
import com.dcode7.iwell.user.enums.UserRequestStatus;
import com.dcode7.iwell.user.transaction.enums.PaymentMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.UserSignUpDTO;
import com.dcode7.iwell.user.UserSignupResponseDTO;
import com.dcode7.iwell.user.password.ChangePasswordDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String userName);

	User registerUser(UserSignUpDTO userSignUpDto, BigDecimal totalDeposite, PaymentMethod paymentMethod, MultipartFile multipartFile);

	User registerFieldAgent(FieldAgentSignUpDTO fieldAgentSignUpDTO);

	UserDetails loadUserByUsername(String user);

	User changePassword(ChangePasswordDTO changePasswordDTO);

	void createAndSendToken(String email);

//	UserResponseDTO getLoggedInUserDetails();

	void createAdminUserIfNotExist();

	UserSignupResponseDTO getRegistrationSuccessDetails(User user, String password);

	void sendRegistrationToken(String email);

	RegistrationOptionsResponseDTO getRegistrationOptions();

	User ResponseForCreatingAccount(UUID UserId, UserRequestStatus userRequestStatus);

}
