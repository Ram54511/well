package com.dcode7.iwell.user.activity;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.activity.deposit.DepositActivity;
import com.dcode7.iwell.user.activity.deposit.DepositActivityInfo;
import com.dcode7.iwell.user.activity.deposit.DepositActivityRepository;
import com.dcode7.iwell.user.activity.deposit.DepositActivityResponse;
import com.dcode7.iwell.user.activity.transfer.TransferActivity;
import com.dcode7.iwell.user.activity.transfer.TransferActivityInfo;
import com.dcode7.iwell.user.activity.transfer.TransferActivityRepository;
import com.dcode7.iwell.user.activity.transfer.TransferActivityResponse;
import com.dcode7.iwell.user.activity.withdraw.WithdrawActivity;
import com.dcode7.iwell.user.activity.withdraw.WithdrawActivityInfo;
import com.dcode7.iwell.user.activity.withdraw.WithdrawActivityRepository;
import com.dcode7.iwell.user.activity.withdraw.WithdrawActivityResponse;
import com.dcode7.iwell.user.login.LoginActivity;
import com.dcode7.iwell.user.login.LoginActivityInfo;
import com.dcode7.iwell.user.login.LoginActivityRepository;
import com.dcode7.iwell.user.login.LoginActivityResponse;

@Service
public class ActivityService {

	@Autowired
	private LoginActivityRepository loginActivityRepository;
	@Autowired
	private TransferActivityRepository transferActivityRepository;
	@Autowired
	private DepositActivityRepository depositActivityRepository;
	@Autowired
	private WithdrawActivityRepository withdrawActivityRepository;
	@Autowired
	private UserActivityRepository userActivityRepository;

	public PaginatedActivityResponseDto getActivityDetails(User user, ActivityType activityType, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

		switch (activityType) {
		case LOGIN:
			return getPaginatedLoginActivityResponseDto(user, pageable, activityType);
		case TRANSFER:
			return getPaginatedTransferActivityResponseDto(user, pageable, activityType);
		case DEPOSIT:
			return getPaginatedDepositActivityResponseDto(user, pageable, activityType);
		case WITHDRAW:
			return getPaginatedWithdrawActivityResponseDto(user, pageable, activityType);
		default:
			throw new IllegalArgumentException("Unsupported activity type");
		}
	}

	public void logActivity(ActivityInfo activityInfo) {
		// don't save activity if activity logging is off
		if (!activityInfo.getUser().getIsActivityLogSaved())
			return;

		switch (activityInfo.getActivityType()) {
		case LOGIN:
			logLoginActivity((LoginActivityInfo) activityInfo);
			break;
		case TRANSFER:
			logTransferActivity((TransferActivityInfo) activityInfo);
			break;
		case DEPOSIT:
			logDepositActivity((DepositActivityInfo) activityInfo);
			break;
		case WITHDRAW:
			logWithdrawActivity((WithdrawActivityInfo) activityInfo);
			break;
		default:
			break;
		}
	}

	private LoginActivityResponse mapToLoginActivityResponse(LoginActivity activity) {
		return new LoginActivityResponse(activity.getTimestamp(), activity.getBrowser(), activity.getIpAddress());
	}

	private TransferActivityResponse mapToTransferActivityResponse(TransferActivity activity) {
		return new TransferActivityResponse(activity.getAmount(), activity.getRecipient().getUsername(),
				activity.getTimestamp());
	}

	private DepositActivityResponse mapToDepositActivityResponse(DepositActivity activity) {
		return new DepositActivityResponse(activity.getTimestamp(), activity.getAmount());
	}

	private WithdrawActivityResponse mapToWithdrawActivityResponse(WithdrawActivity activity) {
		return new WithdrawActivityResponse(activity.getTimestamp(), activity.getAmount());
	}

	private PaginatedActivityResponseDto getPaginatedLoginActivityResponseDto(User user, Pageable pageable,
			ActivityType activityType) {
		Page<LoginActivity> loginActivityPage = loginActivityRepository.findByUser(user, pageable);
		List<ActivityResponse> loginActivities = loginActivityPage.getContent().stream()
				.map(activity -> mapToLoginActivityResponse(activity)).collect(Collectors.toList());

		return new PaginatedActivityResponseDto(activityType, loginActivities, loginActivityPage.getNumber(),
				loginActivityPage.getSize(), loginActivityPage.getTotalElements(), loginActivityPage.getTotalPages());
	}

	private PaginatedActivityResponseDto getPaginatedTransferActivityResponseDto(User user, Pageable pageable,
			ActivityType activityType) {
		Page<TransferActivity> transferActivityPage = transferActivityRepository.findByUser(user, pageable);
		List<ActivityResponse> transferActivities = transferActivityPage.getContent().stream()
				.map(activity -> mapToTransferActivityResponse(activity)).collect(Collectors.toList());

		return new PaginatedActivityResponseDto(activityType, transferActivities, transferActivityPage.getNumber(),
				transferActivityPage.getSize(), transferActivityPage.getTotalElements(),
				transferActivityPage.getTotalPages());
	}

	private PaginatedActivityResponseDto getPaginatedDepositActivityResponseDto(User user, Pageable pageable,
			ActivityType activityType) {
		Page<DepositActivity> depositActivityPage = depositActivityRepository.findByUser(user, pageable);
		List<ActivityResponse> depositActivities = depositActivityPage.getContent().stream()
				.map(activity -> mapToDepositActivityResponse(activity)).collect(Collectors.toList());

		return new PaginatedActivityResponseDto(activityType, depositActivities, depositActivityPage.getNumber(),
				depositActivityPage.getSize(), depositActivityPage.getTotalElements(),
				depositActivityPage.getTotalPages());
	}

	private PaginatedActivityResponseDto getPaginatedWithdrawActivityResponseDto(User user, Pageable pageable,
			ActivityType activityType) {
		Page<WithdrawActivity> withdrawActivityPage = withdrawActivityRepository.findByUser(user, pageable);
		List<ActivityResponse> withdrawActivities = withdrawActivityPage.getContent().stream()
				.map(activity -> mapToWithdrawActivityResponse(activity)).collect(Collectors.toList());

		return new PaginatedActivityResponseDto(activityType, withdrawActivities, withdrawActivityPage.getNumber(),
				withdrawActivityPage.getSize(), withdrawActivityPage.getTotalElements(),
				withdrawActivityPage.getTotalPages());
	}

	private void logLoginActivity(LoginActivityInfo loginActivityInfo) {
		LoginActivity loginActivity = new LoginActivity();
		loginActivity.setUser(loginActivityInfo.getUser());
		loginActivity.setBrowser(loginActivityInfo.getBrowser());
		loginActivity.setIpAddress(loginActivityInfo.getIpAddress());
		loginActivityRepository.save(loginActivity);
	}

	private void logTransferActivity(TransferActivityInfo transferActivityInfo) {
		TransferActivity transferActivity = new TransferActivity();
		transferActivity.setUser(transferActivityInfo.getUser());
		transferActivity.setAmount(transferActivityInfo.getAmount());
		transferActivity.setRecipient(transferActivityInfo.getRecipientUser());
		transferActivityRepository.save(transferActivity);
	}

	private void logDepositActivity(DepositActivityInfo depositActivityInfo) {
		DepositActivity depositActivity = new DepositActivity();
		depositActivity.setUser(depositActivityInfo.getUser());
		depositActivity.setAmount(depositActivityInfo.getAmount());
		depositActivityRepository.save(depositActivity);
	}

	private void logWithdrawActivity(WithdrawActivityInfo activityInfo) {
		WithdrawActivity withdrawActivity = new WithdrawActivity();
		withdrawActivity.setUser(activityInfo.getUser());
		withdrawActivity.setAmount(activityInfo.getAmount());
		withdrawActivityRepository.save(withdrawActivity);
	}

	public Date getLastActivityDateByUser(User user) {
		Activity lastActivity = userActivityRepository.findLastActivityByUserId(user);
		return Objects.nonNull(lastActivity) ? lastActivity.getTimestamp() : null;
	}

}
