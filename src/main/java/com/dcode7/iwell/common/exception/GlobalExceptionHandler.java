package com.dcode7.iwell.common.exception;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
@Order(1)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private LocalService localeService;

	public GlobalExceptionHandler() {
		super();
	}

	private Logger logger1 = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * to handle Illegal Argument Exception
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> handleCustomException(final CustomException ex, final WebRequest request) {
		logger1.error("IllegalArgumentException: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * to handle Illegal Argument Exception
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex,
			final WebRequest request) {
		logger1.error("IllegalArgumentException: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * to handle IOException
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */
	@ExceptionHandler({ IOException.class })
	public ResponseEntity<Object> handleIOException(final IOException ex, final WebRequest request) {
		logger1.error("IOException: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * 
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 **/

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex, final WebRequest request) {
		logger1.error("ConstraintViolationException: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
				localeService.getMessage("Email or mobileNumber already exist"), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleBadRequest(final DataIntegrityViolationException ex, final WebRequest request) {
		logger1.error("DataIntegrityViolationException: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
				localeService.getMessage("Data integrity violation"), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ NullPointerException.class })
	public ResponseEntity<Object> handleNullPointerException(final NullPointerException ex, final WebRequest request) {
		logger1.error("Exception: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		return ResponseHandler.response(HttpStatus.UNAUTHORIZED, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleInternal(final Exception ex, final WebRequest request) {
		logger1.error("500 Status Code");
		logger1.error("Exception: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.INTERNAL_SERVER_ERROR, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * handle insufficien exception
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */
	@ExceptionHandler({ InsufficientInventoryException.class})
	public ResponseEntity<Object> handleInsufficientInventoryException(final Exception ex, final WebRequest request) {
		logger1.error("500 Status Code");
		logger1.error("Exception: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * handle unauthorizedaccess exception
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */
	@ExceptionHandler({ UnauthorizedAccessException.class })
	public ResponseEntity<Object> handleUnauthorizedAccessException(final Exception ex, final WebRequest request) {
		logger1.error("500 Status Code");
		logger1.error("Exception: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * handle resourcenotfound exception
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */
	@ExceptionHandler({ ResourceNotFoundException.class })
	public ResponseEntity<Object> handleResourceNotFoundException(final Exception ex, final WebRequest request) {
		logger1.error("500 Status Code");
		logger1.error("Exception: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

	/**
	 * handle ItemNotFound exception
	 * 
	 * @param ex
	 * @param request
	 * @return ResponseEntity
	 */
	@ExceptionHandler({ ItemNotFoundException.class })
	public ResponseEntity<Object> handleItemNotFoundException(final Exception ex, final WebRequest request) {
		logger1.error("500 Status Code");
		logger1.error("Exception: {}", ex.getLocalizedMessage());
		return ResponseHandler.response(HttpStatus.BAD_REQUEST, true, ex.getMessage(), ErrorCode.ERROR,
				ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT);
	}

}