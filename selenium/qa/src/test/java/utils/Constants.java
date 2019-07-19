package utils;

public class Constants {

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

}
