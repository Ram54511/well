package com.dcode7.iwell.common.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validates `Date` is at least `value` years old. <br>
 * Null field is valid.
 */
@Documented
@Constraint(validatedBy = AgeLimitValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface AgeLimit {

	String message() default "Age must be 18 or older";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int value();
}
