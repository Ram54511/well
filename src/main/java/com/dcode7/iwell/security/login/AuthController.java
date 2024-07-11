package com.dcode7.iwell.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.utils.ErrorCollectionUtil;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class AuthController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private LocalService localeService;

	@PostMapping(value = URLConstant.LOGIN)
	public ResponseEntity<Object> login(@Valid @RequestBody UserSignInForm userSignInForm, BindingResult bindingResult,
			HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
					ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
					ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
		}

		AuthenticationResponseDTO response = authenticationService.authenticate(userSignInForm, request);

		return ResponseHandler.responseWithData(HttpStatus.OK, true, localeService.getMessage("user.login.success"),
				ErrorCode.OK, ResponseCode.ACKNOWLEDGE, response);

	}
}
