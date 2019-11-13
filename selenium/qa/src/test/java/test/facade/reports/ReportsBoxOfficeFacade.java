package test.facade.reports;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import model.Event;
import model.Purchase;
import pages.admin.reports.ReportsBoxOfficeSalesPage;
import pages.admin.reports.ReportsMainPage;
import test.facade.BaseFacadeSteps;
import utils.NGUtils;
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
		System.out.println(dates);
		getReportsBoxOfficePage().enterDateRanges(dates[1], dates[1]);
		NGUtils.getSoftAssert().assertTrue(getReportsBoxOfficePage().checkIfDatesAreCorrect(dates[0], dates[1]),"Dates do not match");
	}
	
	public boolean whenUserSearchesForEventInBoxOfficeReport(Event event) {
		return getReportsBoxOfficePage().isEventInOperatorBoxOfficeSales(event.getEventName());
	}

	@Override
	protected void setData(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object getData(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}