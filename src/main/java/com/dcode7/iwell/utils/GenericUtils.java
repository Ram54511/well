package com.dcode7.iwell.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import com.dcode7.iwell.user.User;

public class GenericUtils {

	private GenericUtils() {
	}

	public static boolean isValidMail(String email) {
//		EmailValidator validator = EmailValidator.getInstance();
//		return validator.isValid(email);

		return true;
	}

	public static User getLoggedInUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static boolean notEquals(Object o1, Object o2) {
		return !o1.equals(o2);
	}

}