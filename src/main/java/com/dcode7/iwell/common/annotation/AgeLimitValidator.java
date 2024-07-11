package com.dcode7.iwell.common.annotation;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeLimitValidator implements ConstraintValidator<AgeLimit, Date> {

	private int ageLimit;

	@Override
	public void initialize(AgeLimit ageLimit) {
		this.ageLimit = ageLimit.value();
	}

	@Override
	public boolean isValid(Date dateOfBirth, ConstraintValidatorContext context) {
		if (dateOfBirth == null) {
			return true;
		}
		// Convert java.util.Date to java.time.LocalDate
		LocalDate dob = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		return Period.between(dob, LocalDate.now()).getYears() >= ageLimit;
	}
}
