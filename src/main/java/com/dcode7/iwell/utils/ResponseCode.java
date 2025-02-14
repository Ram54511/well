/**
 * 
 */
package com.dcode7.iwell.utils;

import org.springframework.lang.Nullable;

/**
 * @author Himanshu Kumar
 *
 * @date 06-Mar-2018
 * 
 * @change log :-
 *
 */
public enum ResponseCode {

	ACKNOWLEDGE(250, "returned response object"), ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT(251,
			"returned response object is optional"), ACKNOWLEDGE_WITHOUT_RESPONSE_OBJECT(252,
					"no need of response object");
	;

	private final int value;

	private final String reasonPhrase;

	ResponseCode(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return Integer.toString(this.value);
	}

	/**
	 * Return the enum constant of this type with the specified numeric value.
	 * 
	 * @param statusCode
	 *            the numeric value of the enum to be returned
	 * @return the enum constant with the specified numeric value
	 * @throws IllegalArgumentException
	 *             if this enum has no constant for the specified numeric value
	 */
	public static ResponseCode valueOf(int statusCode) {
		ResponseCode status = resolve(statusCode);
		if (status == null) {
			throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
		}
		return status;
	}

	/**
	 * Resolve the given status code to an {@code HttpStatus}, if possible.
	 * 
	 * @param statusCode
	 *            the HTTP status code (potentially non-standard)
	 * @return the corresponding {@code HttpStatus}, or {@code null} if not found
	 * @since 5.0
	 */
	@Nullable
	public static ResponseCode resolve(int statusCode) {
		for (ResponseCode status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		return null;
	}

}
