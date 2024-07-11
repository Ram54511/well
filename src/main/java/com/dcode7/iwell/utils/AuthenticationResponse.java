/**
 * 
 */
package com.dcode7.iwell.utils;

/**
 * @author amarjeetkumar
 *
 */

public class AuthenticationResponse {

	private Boolean status = false;

	private String message;

	/**
	 * 
	 */
	/*public AuthenticationResponse() {

	}

	*//**
	 * @param status
	 * @param message
	 *//*
	public AuthenticationResponse(Boolean status, String message) {
		this.status = status;
		this.message = message;
	}*/

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
