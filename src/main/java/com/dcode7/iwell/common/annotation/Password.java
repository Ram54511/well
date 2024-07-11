package com.dcode7.iwell.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

/**
 * Validates password having typical requirements <br>
 * Null field is valid.
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Password {

	String message() default "Password is invalid";

	String messageLength() default "Wrong length of field";

	String messageMustContainOneDigit() default "Must contain at least one digit";

	String messageMustContainOneUpperCaseAlphabet() default "Must contain at least one uppercase alphabet";

	String messageMustContainOneSpecialCharacter() default "Must contain at least one special character";

	String messageMustContainNoWhiteSpace() default "Must not contain whitespaces";

	String messageMustContainOneLowerCaseAlphabet() default "Must contain at least one lowercase alphabet";

	int min() default 0;

	int max() default Integer.MAX_VALUE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
