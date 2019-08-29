package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class ProjectUtils {
	
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String ADMIN_EVENT_DATE_TIME_FORMAT = "EEEE, MMMM d yyyy h:mm a";
	
	public static Integer generateRandomInt(int size) {
		Random random = new Random();
		return random.nextInt(size);
	}

	public static String[] getDatesWithSpecifiedRangeInDays(int spanInDays) {
		return getDatesWithSpecifiedRangeInDaysWithStartOffset(1, spanInDays);
	}
	
	public static String[] getDatesWithSpecifiedRangeInDaysWithStartOffset(int daysOffset, int spanInDays) {
		LocalDate now = LocalDate.now();
		LocalDate newDay = now.plusDays(daysOffset);
		LocalDate nextDate = newDay.plusDays(spanInDays);
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String firstDate = formater.format(newDay);
		String secondDate = formater.format(nextDate);
		String[] retVal = { firstDate, secondDate };
		return retVal;
	}
	
	public static String[] getAllDatesWithinGivenRangeAndOffset(int daysOffset, int spanInDays) {
		LocalDate now = LocalDate.now();
		LocalDate startDate = now.plusDays(daysOffset);
		LocalDate endDate = startDate.plusDays(spanInDays);
		DateRange dateRange = new DateRange(startDate, endDate);
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(DATE_FORMAT);
		List<String> dates = dateRange.toStringList(formater);
		return dates.toArray(new String[dates.size()]);
	}
	
	public static LocalDateTime parseDateTime(String pattern, String dateTime) {
		String removedOrdinalsDate = dateTime.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime localDateTime = LocalDateTime.parse(removedOrdinalsDate, formater);
		return localDateTime;
	}

	public static LocalDate parseDate(String pattern, String date) {
		String removedOrdinalsDate = date.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(pattern);
		LocalDate localDate = LocalDate.parse(removedOrdinalsDate, formater);
		return localDate;
	}

	public static LocalDateTime getDateTime(LocalDate date) {
		LocalDateTime dt = date.atTime(LocalTime.MIDNIGHT);
		return dt;
	}

	public static void appendField(String field, StringBuilder sb) {
		sb.append(field != null ? field + "; " : "");
	}

	public static void appendFields(String[] fields , StringBuilder sb) {
		for(String f : fields) {
			appendField(f, sb);
		}
	}
}
