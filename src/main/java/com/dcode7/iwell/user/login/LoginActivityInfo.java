package com.dcode7.iwell.user.login;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.activity.ActivityInfo;
import com.dcode7.iwell.user.activity.ActivityType;
import lombok.Data;

@Data
public class LoginActivityInfo extends ActivityInfo {

	private String browser;

	private String ipAddress;

	public LoginActivityInfo(User user, String browser, String ipAddress) {
		super(user, ActivityType.LOGIN);
		this.browser = browser;
		this.ipAddress = ipAddress;
	}

}
