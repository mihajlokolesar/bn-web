package test.facade.reports;

import java.time.LocalDate;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.Event;
import pages.admin.reports.ReportsBoxOfficeSalesPage;
import pages.admin.reports.ReportsMainPage;
import test.facade.BaseFacadeSteps;
import utils.ProjectUtils;

public class ReportsBoxOfficeFacade extends BaseFacadeSteps {
	
	private ReportsMainPage reportsMainPage;
	private ReportsBoxOfficeSalesPage reportsBoxOfficePage;

	public ReportsBoxOfficeFacade(WebDriver driver) {
		super(driver);
	}
	
	public ReportsMainPage getReportsMainPage() {
		return this.reportsMainPage != null ? this.reportsMainPage : 
			(this.reportsMainPage = new ReportsMainPage(driver));
	}

	public ReportsBoxOfficeSalesPage getReportsBoxOfficePage() {
		return this.reportsBoxOfficePage != null ? this.reportsBoxOfficePage : 
			(this.reportsBoxOfficePage = new ReportsBoxOfficeSalesPage(driver));
	}
	
	public void enterDates() {
		LocalDate now = LocalDate.now();
		String[] dates = ProjectUtils.getDatesWithSpecifiedRangeInDaysWithStartOffset(-1, 1);
		getReportsBoxOfficePage().enterDateRanges(dates[1], dates[1]);
		Assert.assertTrue(getReportsBoxOfficePage().checkIfDatesAreCorrect(dates[1], dates[1]),"Dates do not match");
	}
	
	public boolean whenUserSearchesForEventInBoxOfficeReport(Event event) {
		return getReportsBoxOfficePage().isEventInOperatorBoxOfficeSales(event.getEventName());
	}

	@Override
	protected void setData(String key, Object value) {
	}

	@Override
	protected Object getData(String key) {
		return null;
	}
}