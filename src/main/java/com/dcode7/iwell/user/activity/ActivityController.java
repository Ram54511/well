package com.dcode7.iwell.user.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dcode7.iwell.constant.URLConstant;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.utils.GenericUtils;

@RestController
@RequestMapping(URLConstant.USER_ACTIVITIES)
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@GetMapping()
	public ResponseEntity<?> getActivityDetails(@RequestParam(name = "type") ActivityType activityType,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		User user = GenericUtils.getLoggedInUser();
		try {
			PaginatedActivityResponseDto activities = activityService.getActivityDetails(user, activityType, page,
					size);
			return ResponseEntity.ok(activities);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
