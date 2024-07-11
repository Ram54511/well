package com.dcode7.iwell.utils;

public interface SMSService {

	/**
	 * This method is use to send Message
	 * 
	 * @param mobileNumber
	 * @param countryCode
	 * @param msg
	 */
	void sendMessage(String mobileNumber, String countryCode, String msg);

	/**
	 * This method is use to send Otp
	 * 
	 * @param otp
	 * @param countryCode
	 * @param mobileNumber
	 */
	void sendOtp(int otp, Long countryCode, String mobileNumber);
	
	

}
