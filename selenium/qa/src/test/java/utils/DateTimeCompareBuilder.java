package utils;

import java.time.LocalDateTime;

public class DateTimeCompareBuilder {

	private boolean compareYear;
	private boolean compareMonth;
	private boolean compareDayOfMonth;
	private boolean compareDayOfWeek;
	private boolean compareHour;
	private boolean compareMinute;

	public DateTimeCompareBuilder() {
		super();
	}

	public DateTimeCompareBuilder compareYear() {
		this.compareYear = true;
		return this;
	}

	public DateTimeCompareBuilder compareMonth() {
		this.compareMonth = true;
		return this;
	}

	public DateTimeCompareBuilder compareDayOfMonth() {
		this.compareDayOfMonth = true;
		return this;
	}

	public DateTimeCompareBuilder compareDayOfWeek() {
		this.compareDayOfWeek = true;
		return this;
	}

	public DateTimeCompareBuilder compareHour() {
		this.compareHour = true;
		return this;
	}

	public DateTimeCompareBuilder compareMinute() {
		this.compareMinute = true;
		return this;
	}

	public boolean compare(LocalDateTime first, LocalDateTime second) {
		boolean retVal = true;
		if (first == null) {
			throw new IllegalArgumentException("First argument in: " + getClass().getName() + " is null");
		}
		if (second == null) {
			throw new IllegalArgumentException("Second argument in: " + getClass().getName() + " is null");
		}

		if (compareYear)
			retVal = assign(retVal, first.getYear() == second.getYear());
		if (compareMonth)
			retVal = assign(retVal, first.getMonth().compareTo(second.getMonth()) == 0);
		if (compareDayOfMonth)
			retVal = assign(retVal, first.getDayOfMonth() == second.getDayOfMonth());
		if (compareDayOfWeek)
			retVal = assign(retVal, first.getDayOfWeek().equals(second.getDayOfWeek()));
		if (compareHour)
			retVal = assign(retVal, first.getHour() == second.getHour());
		if (compareMinute)
			retVal = assign(retVal, first.getMinute() == second.getMinute());

		return retVal;
	}

	private boolean assign(boolean starting, boolean additional) {
		return starting && additional;
	}

}
