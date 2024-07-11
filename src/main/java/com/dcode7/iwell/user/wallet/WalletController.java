package com.dcode7.iwell.user.wallet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcode7.iwell.common.enums.ErrorCode;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.wallet.service.WalletService;
import com.dcode7.iwell.utils.GenericUtils;
import com.dcode7.iwell.utils.ResponseCode;
import com.dcode7.iwell.utils.ResponseHandler;

@RestController
public class WalletController {

	@Autowired
	private WalletService walletService;

	@Autowired
	private LocalService localeService;

	@GetMapping(URLConstant.USER_WALLETS)
	public ResponseEntity<?> getMyWallets() {

		User user = GenericUtils.getLoggedInUser();

		List<WalletResponseDto> wallets = walletService.getWalletsByUser(user);

		return ResponseHandler.responseWithData(HttpStatus.OK, true,
				localeService.getMessage("wallet.fetched.successfully"), ErrorCode.OK, ResponseCode.ACKNOWLEDGE,
				wallets);

	}

}
