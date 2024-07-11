package com.dcode7.iwell.security.login;

import java.util.Optional;

import com.dcode7.iwell.user.enums.UserRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.security.factory.JWTTokenFactory;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.activity.ActivityService;
import com.dcode7.iwell.user.login.LoginActivityInfo;
import com.dcode7.iwell.user.service.UserService;
import com.dcode7.iwell.utils.PasswordEncoderUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

	@Autowired
	private UserService userService;

	@Autowired
	private LocalService localeService;

	@Autowired
	private JWTTokenFactory jwtTokenFactory;

	@Autowired
	private PasswordEncoderUtil passwordEncoderUtil;

	@Autowired
	private ActivityService activityService;

	public AuthenticationResponseDTO authenticate(UserSignInForm userSignInForm, HttpServletRequest request) {
		User user = validateUserCredentialsAndGetUser(userSignInForm);
		validateUserStatus(user);

		JWTAuthenticationResponse jwtAuthenticationData = createAuthenticationData(user.getEmail());

		activityService
				.logActivity(new LoginActivityInfo(user, request.getHeader("User-Agent"), request.getRemoteAddr()));

		return buildAuthenticationResponse(jwtAuthenticationData, user);
	}

	private void validateUserStatus(User user) {

		if (!user.getIsActive()) {
			throw new CustomException(localeService.getMessage("user.login.user.not.active"), HttpStatus.FORBIDDEN);
		}

		if (!user.getIsEmailVerified()) {
			throw new CustomException(localeService.getMessage("user.login.email.not.verified"), HttpStatus.FORBIDDEN);
		}

		if (user.getIsUserBlocked()) {
			throw new CustomException(localeService.getMessage("user.login.user.blocked"), HttpStatus.FORBIDDEN);
		}

		if (user.getIsDeleted()) {
			throw new CustomException(localeService.getMessage("user.login.user.deleted"), HttpStatus.FORBIDDEN);
		}

		if (!user.getIsRegistered()) {
			throw new CustomException(localeService.getMessage("user.login.user.not.registered"), HttpStatus.FORBIDDEN);
		}

		if (user.getUserRequestStatus() == UserRequestStatus.PENDING || user.getUserRequestStatus() == UserRequestStatus.REJECTED) {
			throw new CustomException(localeService.getMessage("user.request.not.approved"), HttpStatus.FORBIDDEN);
		}

	}

	private User validateUserCredentialsAndGetUser(UserSignInForm userSignInForm) {

		String emailOrUsername = userSignInForm.getEmailOrUsername().strip();

		Optional<User> optionalUser = userService.findByEmail(emailOrUsername);

		if (optionalUser.isEmpty()) {
			optionalUser = userService.findByUsername(emailOrUsername);
		}

		if (optionalUser.isEmpty() || optionalUser.isPresent()
				&& !passwordEncoderUtil.matches(userSignInForm.getPassword(), optionalUser.get().getPassword())) {
			throw new CustomException(localeService.getMessage("user.login.credentials.invalid"),
					HttpStatus.BAD_REQUEST);
		}

		return optionalUser.get();

	}

	private JWTAuthenticationResponse createAuthenticationData(String email) {
		return jwtTokenFactory.createTokenPair(email);
	}

	private AuthenticationResponseDTO buildAuthenticationResponse(JWTAuthenticationResponse jwtAuthenticationData,
			User user) {
		return new AuthenticationResponseDTO(jwtAuthenticationData.getAccessToken(),
				jwtAuthenticationData.getRefreshToken(), user.getFullName());
	}

}
