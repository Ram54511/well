package com.dcode7.iwell.user.login;

import java.util.Date;

import com.dcode7.iwell.user.activity.ActivityResponse;
import com.dcode7.iwell.user.activity.ActivityType;

import lombok.Data;

@Data
public class LoginActivityResponse extends ActivityResponse {

	private String browser;

	private String ipAddress;

	public LoginActivityResponse(Date timestamp, String browser, String ipAddress) {
		super(timestamp, ActivityType.LOGIN);
		this.browser = browser;
		this.ipAddress = ipAddress;
	}


}