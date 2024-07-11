package com.dcode7.iwell.constant;

public abstract class IwellConstant {

	public static final String JWT_SECRET = "6683489786446T37747T4T7577377R6833574439R88R6764766588RR57636349R876489385368687893963365562463276375487375";

	public static final long JWT_EXPIRATION = 86400000L;

	public static final String USERNAME_PREFIX = "DPK";

	// pattern = (optional country_code)(optional hyphen with whitespace on either
	// side) number
	public static final String MOBILE_NUMBER_VALIDATION_REGEX = "^((00|\\+|0|)91)?\s*-?\s*[6-9]\\d{9}$";

	public static final String MOBILE_NUMBER_CAPTURE_REGEX = "(\\d{10})$";

}
