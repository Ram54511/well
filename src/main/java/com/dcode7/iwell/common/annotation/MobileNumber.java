package com.dcode7.iwell.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validation of Indian mobile numbers with 10 digits and optional leading zeros
 * & country code
 * <p>
 * Null field is valid.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileNumberValidator.class)
public @interface MobileNumber {

	String message() default "Invalid mobile number";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
