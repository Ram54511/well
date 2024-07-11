/**
 * 
 */
package com.dcode7.iwell.utils;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Himanshu Kumar
 *
 */
public class ErrorCollectionUtil {

	private static final String DEFAULT_ERROR_MESSAGE = "Wrong data requested";

	private ErrorCollectionUtil() {
	}

	public static Map<String, Object> getErrorMap(BindingResult bindingResult) {
		Map<String, Object> errors = new TreeMap<>();

		bindingResult.getFieldErrors()
				.forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
		return errors;
	}

	public static <T> String getError(BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return DEFAULT_ERROR_MESSAGE;
		}
		return null;

	}
	
	/**
	 * To  Return single first error from all errors so as to show in message field
	 * @param bindingResult
	 * @return
	 */
	public static <T> String getErrorMessage(BindingResult bindingResult) {
			for(FieldError fieldError:bindingResult.getFieldErrors()) {
	        	return  fieldError.getDefaultMessage();
	         }
		return null;

	}
}
