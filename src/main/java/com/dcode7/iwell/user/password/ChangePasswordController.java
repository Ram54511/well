package com.dcode7.iwell.user.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.user.service.UserService;
import com.dcode7.iwell.utils.ErrorCollectionUtil;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;

@RestController
public class ChangePasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	private LocalService localService;

	@PostMapping(value = URLConstant.CHANGE_PASSWORD)
	public ResponseEntity<Object> changeUserPassword(@Validated @RequestBody ChangePasswordDTO changePasswordDTO,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseHandler.response(HttpStatus.BAD_REQUEST, true,
					ErrorCollectionUtil.getErrorMessage(bindingResult), ErrorCode.ERROR,
					ResponseCode.ACKNOWLEDGE_OPTIONAL_RESPONSE_OBJECT, ErrorCollectionUtil.getErrorMap(bindingResult));
		}

		userService.changePassword(changePasswordDTO);
		return ResponseHandler.responseWithoutData(HttpStatus.OK, true,
				localService.getMessage("user.password.change.success"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE);

	}

}