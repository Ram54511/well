package com.dcode7.iwell.user.activity;

import java.util.List;

public class PaginatedActivityResponseDto {

	private ActivityType type;

	private List<ActivityResponse> activities;

	private int pageNumber;
	
	private int pageSize;
	
	private long totalElements;
	
	private int totalPages;

	public PaginatedActivityResponseDto() {}

	public PaginatedActivityResponseDto(ActivityType type, List<ActivityResponse> activities, int pageNumber, int pageSize,
			long totalElements, int totalPages) {
		super();
		this.type = type;
		this.activities = activities;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public List<ActivityResponse> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityResponse> activities) {
		this.activities = activities;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
