package com.dcode7.iwell.user.login;

import com.dcode7.iwell.user.activity.Activity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class LoginActivity extends Activity {

	private String ipAddress;

	private String browser;

}
