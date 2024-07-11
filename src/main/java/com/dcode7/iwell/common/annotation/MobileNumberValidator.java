package com.dcode7.iwell.common.annotation;

import java.util.regex.Pattern;

import com.dcode7.iwell.constant.IwellConstant;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobileNumberValidator implements ConstraintValidator<MobileNumber, String> {

	private String message;

	@Override
	public void initialize(MobileNumber mobileNumber) {
		message = mobileNumber.message();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value != null && !Pattern.matches(IwellConstant.MOBILE_NUMBER_VALIDATION_REGEX, value.strip())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
			return false;
		}

		return true;
	}

}
