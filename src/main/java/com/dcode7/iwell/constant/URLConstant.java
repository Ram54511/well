package com.dcode7.iwell.constant;

public class URLConstant {

	private URLConstant() {
	}

	public static final String BASEURL = "/api/v1";
	public static final String REGISTER = BASEURL + "/register";
	public static final String FIELDAGENT_REGISTER = BASEURL + "/fieldagentregister";
	public static final String USER_CREATE_RESPONSE = BASEURL + "/response";
	public static final String LOGIN = BASEURL + "/login";
	public static final String TOKEN_SECRET = "my token secret";

	public static final String PINCODE = BASEURL + "/pincode";

//	public static final String SEND_EMAIL_ON_REGISTRATION = BASEURL + "sendRegistrationToken";
	public static final String USER = BASEURL + "/user";
	public static final String CHANGE_PASSWORD = BASEURL + "/changePassword";
	public static final String MAKE_TRANSACTION = USER + "/makeTransaction";
	public static final String MAKE_DEPOSIT = USER + "/makeDeposit";
	public static final String REGISTRATION_TOKEN = USER + "/registrationToken";
	public static final String USER_DETAILS = USER + "/details";

//	public static final String USER_FUND_TRANSFER = USER + "/transfer";
//	public static final String USER_FUND_TRANSFER_REQUEST = USER_FUND_TRANSFER + "/request";
//	public static final String USER_FUND_TRANSFER_AUTHENTICATE = USER_FUND_TRANSFER + "/authenticate";
//	public static final String USER_FUND_TRANSFER_RESEND_OTP = USER_FUND_TRANSFER + "/resendOtp";

//	public static final String USER_WITHDRAW = USER + "/withdraw";
//	public static final String USER_WITHDRAW_REQUEST = USER_WITHDRAW + "/request";
//	public static final String USER_WITHDRAW_AUTHENTICATE = USER_WITHDRAW + "/authenticate";
//	public static final String USER_WITHDRAW_RESEND_OTP = USER_WITHDRAW + "/resendOtp";

	public static final String USER_WALLETS = USER + "/wallets";

//	public static final String MY_WALLETS = USER + "/myWallets";

	public static final String ADMIN = BASEURL + "/admin";

//	public static final String ADMIN_GET_DEPOSITS = ADMIN + "/allDeposits";
//
//	public static final String ADMIN_APPROVE_DEPOSIT = ADMIN + "/approveDeposit";
//	
//	public static final String ADMIN_GET_WITHDRAWALS = ADMIN + "/allWithdrawals";
//
//	public static final String ADMIN_APPROVE_WITHDRAWAL = ADMIN + "/approveWithdrawal";

//	public static final String USER_SETTINGS = USER + "/settings";

	public static final String USER_ACTIVITIES = USER + "/activities";

//	public static final String USER_SETTINGS_TOGGLE_SAVE_ACTIVITY_LOGS = USER_SETTINGS + "/toggleSaveActivityLogs";

//	public static final String USER_TRADING = USER + "/tradingPackages";
//	public static final String BUY_PACKAGE = USER + "/buyPackage";
//	public static final String MY_PACKAGES = USER + "/myPackages";

	public static final String USER_DASHBOARD = USER + "/dashboard";

	public static final String USER_SIDEBAR = USER + "/sidebar";

	public static final String USER_REFERRAL_EARNING = USER + "/myReferralEarning";

	public static final String REFERRAL_DETAILS = BASEURL + "/referralDetails";

//	public static final String USERNAME_AVAILABLE = BASEURL + "/usernameAvailable";

	// inventory categories
	public static final String INVENTORY_CATEGORIES = BASEURL + "/inventorycategory";
	public static final String ADMIN_ADD_INVENTORY_CATEGORIES = INVENTORY_CATEGORIES + "/createCategory";
	public static final String ADMIN_UPDATE_INVENTORY_CATEGORIES = INVENTORY_CATEGORIES + "/updateCategory/";
	public static final String ADMIN_GET_INVENTORY_CATEGORIES = BASEURL + "/getInventoryCategoryById/";
	public static final String GET_ALL_INVENTORY_CATEGORIES = BASEURL + "/getAllInventoryCategory";
	public static final String SOFT_DELETE_INVENTORY_CATEGORIES = BASEURL + "/getAllInventoryCategory/";

	// inventory item
	public static final String INVENTORY_ITEM = BASEURL + "/inventoryitem";
	public static final String ADMIN_ADD_INVENTORY_ITEM = INVENTORY_ITEM + "/createItem";
	public static final String ADMIN_UPDATE_INVENTORY_ITEM = INVENTORY_ITEM + "/updateCategory/";
	public static final String ADMIN_GET_INVENTORY_ITEM = INVENTORY_ITEM + "/getInventoryItemById/";
	public static final String GET_ALL_INVENTORY_ITEM = INVENTORY_ITEM + "/getAllInventoryItem";
	public static final String SOFT_DELETE_INVENTORY_ITEM = INVENTORY_ITEM + "/getAllInventoryItem/";

	// inventory item
	public static final String INVENTORY_REQUEST = BASEURL + "/inventoryRequest";
	public static final String CREATE_INVENTORY_REQUEST = INVENTORY_ITEM + "/createInventoryRequest";
	public static final String UPDATE_INVENTORY_REQUEST = INVENTORY_ITEM + "/updateInventoryRequest/";
	public static final String GET_INVENTORY_REQUEST = INVENTORY_ITEM + "/getInventoryRequestById/";
	public static final String GET_ALL_INVENTORY_REQUEST = INVENTORY_ITEM + "/getAllInventoryRequest";
	public static final String SOFT_DELETE_INVENTORY_REQUEST = INVENTORY_ITEM + "/softDeleteInventoryRequest/";
	public static final String INVENTORY_RESPONSE = BASEURL + "/inventoryResponse/";

	// inventory return request
	public static final String INVENTORY_RETURN = BASEURL + "/inventoryreturn";
	public static final String CREATE_INVENTORY_RETURN_REQUEST = INVENTORY_RETURN + "/createInventoryRequest";
	public static final String RESPONSE_INVENTORY_RETURN_REQUEST = INVENTORY_RETURN + "/responseInventoryRequest/";

	// inventory return request
	public static final String TAX = BASEURL + "/tax";
	public static final String TAX_UPDATE = TAX + "/updatetax/";
	public static final String GET_ALL_TAX = TAX + "/getAll";

	// CNF
	public static final String LOGIN_CNF_FIELDAGENT = BASEURL + "/getAllFieldAgentOfLoginCNF";
	public static final String LOGIN_CNF_REQUEST = BASEURL + "/getAllRequestOfLoginCNF";
	public static final String GET_INVENTORY_REQUEST_BY_ID = BASEURL + "/getInventoryRequestById/";
	public static final String GET_INVENTORY_REQUEST_TO_LOGIN_CNF = BASEURL + "/getInventoryRequestToLoginCNF";

	//	fieldagent
	public static final String INVENTORY_ITEM_FIELDAGENT_CNF = BASEURL + "/getAllInventoryItem";
	public static final String APPROVED_REQUEST_BY_CNF = BASEURL + "/response/";
	public static final String FIELDAGENT_INVENTORY_REQUEST = BASEURL + "/fieldagentInventoryRequest";
	public static final String FIELDAGENT_INVOICES = BASEURL + "/fieldagentInvoices";

	//	customer
	public static final String CUSTOMER = BASEURL + "/createCustomer";

}
