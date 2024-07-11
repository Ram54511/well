package com.dcode7.iwell.user.activity;

import java.util.Date;

public abstract class ActivityResponse {
	
	private Date timestamp;
	
	private ActivityType type;
	
	public ActivityResponse(Date timestamp, ActivityType type) {
		super();
		this.timestamp = timestamp;
		this.type = type;
	}
	
	public ActivityResponse() {}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public ActivityType getType() {
		return type;
	}

}
