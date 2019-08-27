package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class ProjectUtils {
	
	public static final String DATE_FORMAT = "MM/dd/yyyy";

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
	
}
