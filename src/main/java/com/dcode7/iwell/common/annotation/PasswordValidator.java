package com.dcode7.iwell.common.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	private Integer min;
	private Integer max;
	private String messageLength;
	private String messageMustContainOneDigit;
	private String messageMustContainOneLowerCaseAlphabet;
	private String messageMustContainOneUpperCaseAlphabet;
	private String messageMustContainNoWhiteSpace;
	private String messageMustContainOneSpecialCharacter;

	@Override
	public void initialize(Password field) {
		min = field.min();
		max = field.max();
		messageLength = field.messageLength();
		messageMustContainOneDigit = field.messageMustContainOneDigit();
		messageMustContainOneLowerCaseAlphabet = field.messageMustContainOneLowerCaseAlphabet();
		messageMustContainOneUpperCaseAlphabet = field.messageMustContainOneUpperCaseAlphabet();
		messageMustContainNoWhiteSpace = field.messageMustContainNoWhiteSpace();
		messageMustContainOneSpecialCharacter = field.messageMustContainOneSpecialCharacter();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		if (value == null) {
			return true;
		}

		HibernateConstraintValidatorContext context = constraintValidatorContext
				.unwrap(HibernateConstraintValidatorContext.class);
		context.disableDefaultConstraintViolation();

		if (value.length() < min || value.length() > max) {
			context.addMessageParameter("min", min).addMessageParameter("max", max)
					.buildConstraintViolationWithTemplate(messageLength).addConstraintViolation();
			return false;
		}

		if (!value.matches("(?=.*[0-9]).+")) {
			context.buildConstraintViolationWithTemplate(messageMustContainOneDigit).addConstraintViolation();
			return false;
		}

		Pattern lowerCasePattern = Pattern.compile("\\p{Ll}");
		Matcher lowerCaseMatcher = lowerCasePattern.matcher(value);
		if (!lowerCaseMatcher.find()) {
			context.buildConstraintViolationWithTemplate(messageMustContainOneLowerCaseAlphabet)
					.addConstraintViolation();
			return false;
		}

		Pattern upperCasePattern = Pattern.compile("\\p{Lu}");
		Matcher upperCaseMatcher = upperCasePattern.matcher(value);
		if (!upperCaseMatcher.find()) {
			context.buildConstraintViolationWithTemplate(messageMustContainOneUpperCaseAlphabet)
					.addConstraintViolation();
			return false;
		}

		boolean whiteSpace = false;
		for (int i = 0; i < value.length(); i++) {
			if (Character.isWhitespace(value.charAt(i))) {
				whiteSpace = true;
				break;
			}
		}
		if (whiteSpace) {
			context.buildConstraintViolationWithTemplate(messageMustContainNoWhiteSpace).addConstraintViolation();
			return false;
		}

		// make sure there is at least one special character
		Pattern specialCharactersPattern = Pattern.compile("[^\\p{Alnum}\\s]");
		Matcher specialCharactersMatcher = specialCharactersPattern.matcher(value);
		if (!specialCharactersMatcher.find()) {
			context.buildConstraintViolationWithTemplate(messageMustContainOneSpecialCharacter)
					.addConstraintViolation();
			return false;
		}

		return true;
	}

}
