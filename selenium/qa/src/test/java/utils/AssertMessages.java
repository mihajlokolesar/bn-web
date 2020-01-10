package utils;

public class AssertMessages {
	
	public static String ORGANIZATION_PER_ORDER_FEE_UPDATED_FAIL = "Organization, per order fee update failed, orgId: ";
	
	public static void main(String[] args) {
		String splitRegex = "\\(\\+?\\d+\\)";
		String text = "Email all current ticket holders (0) to announce any major event updates including cancellation, postponement, rescheduled date/time, or new location. Disclaimer: Not intended for marketing purposes.";
		String[] tokens = text.split(splitRegex);
		System.out.println();
	}
}