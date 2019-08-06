package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ProjectUtils {

	public static Integer generateRandomInt(int size) {
		Random random = new Random();
		return random.nextInt(size);
	}

	public static String[] getDatesWithSpecifiedRangeInDays(long spanInDays) {
		LocalDate now = LocalDate.now();
		LocalDate newDay = now.plusDays(1);
		LocalDate twoDayForward = now.plusDays(spanInDays + 1);
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String firstDate = formater.format(newDay);
		String secondDate = formater.format(twoDayForward);
		String[] retVal = { firstDate, secondDate };
		return retVal;
	}

}
