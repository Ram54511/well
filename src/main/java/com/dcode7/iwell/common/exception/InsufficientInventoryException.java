package com.dcode7.iwell.common.exception;

import org.springframework.http.HttpStatus;

public class InsufficientInventoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final HttpStatus status;
	private final String message;

	public InsufficientInventoryException(String message, HttpStatus status) {
		super(message);
		this.status = status;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
