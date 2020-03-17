package utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

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
	
	public static String getMyEventsBigNeon() {
		return getBaseUrlBigNeon() + "my-events";
	}

	public static String getEventsBigNeon() {
		return getBaseUrlBigNeon() + "events";
	}

	public static String getBoxOfficeSell() {
		return getBaseUrlBigNeon() + "box-office/sell";
	}
	
	public static String getBoxOfficeGuest() {
		return getBaseUrlBigNeon() + "box-office/guests";
	}

	public static String getAdminEvents() {
		return getBaseUrlBigNeon() + "admin/events";
	}

	public static String getAdminOrganizations() {
		return getBaseUrlBigNeon() + "admin/organizations";
	}
	
	public static String getAdminVenues() {
		return getBaseUrlBigNeon() + "admin/venues";
	}
	
	public static String getAdminVenueCreate() {
		return getAdminVenues() + "/create";
	}

	public static String getAdminOrganizationsCreate() {
		return getAdminOrganizations() + "/create";
	}

	public static String getAdminEventCreate() {
		return getAdminEvents() + "/create";
	}
	
	public static String getAdminFans() {
		return getBaseUrlBigNeon() + "admin/fans";
	}	
	
	public static String getAdminReports() {
		return getBaseUrlBigNeon() + "admin/reports";
	}
	
	public static String getAdminReportsBoxOfficeSale() {
		return getAdminReports() + "/box-office-sales-summary";
	}
}
