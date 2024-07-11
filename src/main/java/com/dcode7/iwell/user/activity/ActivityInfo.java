package com.dcode7.iwell.user.activity;

import com.dcode7.iwell.user.User;
import lombok.Data;

/**
 * Extend this class to define activity specific details when logging the any
 * activity
 */
@Data
public abstract class ActivityInfo {

	private User user;

	private ActivityType activityType;

	public ActivityInfo(User user, ActivityType activityType) {
		super();
		this.user = user;
		this.activityType = activityType;
	}

	public User getUser() {
		return user;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

}
