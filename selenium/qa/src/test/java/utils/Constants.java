package utils;

public class Constants {
	
	
	public static final String MAILINATOR_BASE_URL = "https://www.mailinator.com/";

	public static String getBaseUrlBigNeon() {
		String baseUrl = System.getProperty("baseurl");
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		return baseUrl;
	}

	public static String getLoginUrlBigNeon() {
		return getBaseUrlBigNeon() + "login";
	}

	public static String getSignUpBigNeon() {
		return getBaseUrlBigNeon() + "sign-up";
	}
	
	public static String getResetPasswordBigNeon() {
		return getBaseUrlBigNeon() + "password-reset";
	}
	
	public static String getAccountBigNeon() {
		return getBaseUrlBigNeon() + "account";
	}

}
