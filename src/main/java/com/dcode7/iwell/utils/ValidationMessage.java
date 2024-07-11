package com.dcode7.iwell.utils;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * The Class ValidationMessage.
 */
@Component
public class ValidationMessage {

	/**
	 * Gets the field error response.
	 *
	 * @param result
	 *            the result
	 * @return the field error response
	 */
	public String getFieldErrorResponse(BindingResult result) {
		List<FieldError> errors = result.getFieldErrors();
		return errors.get(0).getDefaultMessage();

	}

}
