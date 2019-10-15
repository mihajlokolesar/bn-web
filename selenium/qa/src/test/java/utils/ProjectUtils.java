package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import pages.interfaces.Visible;

public class ProjectUtils {

	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String TIME_FORMAT = "h:mm a";
	public static final String CONCATINATED_DATE_FORMAT = "MMddyyyy";
	public static final String ADMIN_EVENT_DATE_TIME_FORMAT = "EEEE, MMMM d yyyy h:mm a";
	public static final String MANAGE_ORDER_HISTORY_ITEM_DATE_FORMAT = "EEE, MMM d, yyyy h:mm a";
	public static final String RESOURCE_IMAGE_PATH = "src/test/resources/images/";

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
	
	public static LocalDateTime getLocalDateTime(String datePattern, String date, String timePattern, String time) {
		LocalDate localDate = ProjectUtils.parseDate(datePattern, date);
		LocalTime localTime = ProjectUtils.parseTime(timePattern, time);
		LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
		return ldt;
	}

	public static LocalDate parseDate(String pattern, String date) {
		String removedOrdinalsDate = date.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(pattern);
		LocalDate localDate = LocalDate.parse(removedOrdinalsDate, formater);
		return localDate;
	}
		
	public static LocalTime parseTime(String pattern, String time) {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(pattern);
		LocalTime localTime = LocalTime.parse(time, formater);
		return localTime;
	}
	
	public static LocalDateTime getDateTime(LocalDate date) {
		LocalDateTime dt = date.atTime(LocalTime.MIDNIGHT);
		return dt;
	}

	public static void appendField(String field, StringBuilder sb) {
		sb.append(field != null ? field + "; " : "");
	}

	public static void appendFields(String[] fields, StringBuilder sb) {
		for (String f : fields) {
			appendField(f, sb);
		}
	}

	public static boolean isNumberGreaterThan(Integer number, Integer greaterThan) {
		if (number != null && greaterThan != null) {
			if (number.compareTo(greaterThan) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public static Object[] createAndFillArrayWithObject(int size, Object original) {
		Object[] dest = new Object[size];
		Arrays.fill(dest, original);
		return dest;
	}

	public static Object[][] composeData(Object[][] dest, Object[] src, int destinationColumn) {
		for (int i = 0; i < src.length; i++) {
			dest[i][destinationColumn] = src[i];
		}
		return dest;
	}

	public static String getTextForElementAndReplace(WebElement element, String oldChar, String newChar) {
		String text = element.getText();
		return text.replace(oldChar, newChar);

	}

	public static String getImageAbsolutePath(String imageName) {
		File file = new File(RESOURCE_IMAGE_PATH + imageName);
		if (file.exists()) {
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}

	/**
	 * It follows convention that suffix is last "_" found, that is discarder and
	 * replaced with current date sequence. If no "_" is found date is appended to given 
	 * text parametar with "_" between date and text
	 * 
	 * @param text
	 * @return
	 */
	public static String setSuffixDateOfText(String text) {
		int index = text.lastIndexOf("_");
		String baseName = text;
		if (index != -1) {
			baseName = text.substring(0, index);
		}
		LocalDate now = LocalDate.now();
		DateTimeFormatter formater = DateTimeFormatter.ofPattern(CONCATINATED_DATE_FORMAT);
		String date = now.format(formater);
		
		return baseName + "_" + date;
	}

	public static String getId(String urlPath, String match) {
		int index = urlPath.indexOf(match);
		String id = urlPath.substring(index + match.length());
		return id;
	}

	public static Visible getVisibleComponent(Visible visible) {
		if (visible.isVisible()) {
			return visible;
		} else {
			throw new NoSuchElementException("Element for component:" + visible.getClass() + "not found");
		}
	}

}
