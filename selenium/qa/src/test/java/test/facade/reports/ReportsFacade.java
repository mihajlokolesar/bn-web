package test.facade.reports;

import org.openqa.selenium.WebDriver;

import model.Event;
import pages.admin.reports.ReportsBoxOfficeSalesPage;
import pages.admin.reports.ReportsMainPage;
import pages.components.admin.AdminSideBar;
import test.facade.BaseFacadeSteps;

public class ReportsFacade extends BaseFacadeSteps {
	
	private ReportsMainPage reportsMainPage;
	private ReportsBoxOfficeSalesPage reportsBoxOfficePage;
	private AdminSideBar adminSideBar;
	
	public ReportsFacade(WebDriver driver) {
		super(driver);
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

	public AdminSideBar getAdminSideBar() {
		return this.adminSideBar != null ? this.adminSideBar :
			(this.adminSideBar = new AdminSideBar(driver));
	}
	
	public ReportsMainPage getReportsMainPage() {
		return this.reportsMainPage != null ? this.reportsMainPage : 
			(this.reportsMainPage = new ReportsMainPage(driver));
	}

	public ReportsBoxOfficeSalesPage getReportsBoxOfficePage() {
		return this.reportsBoxOfficePage != null ? this.reportsBoxOfficePage : 
			(this.reportsBoxOfficePage = new ReportsBoxOfficeSalesPage(driver));
	}
	
	public void givenUserIsOnReportsPage() {
		getAdminSideBar().clickOnReports();
		getReportsMainPage().isAtPage();
	}
	
	public void whenUserSelectBoxOfficeTab() {
		getReportsMainPage().clickOnBoxOfficeSales();
		getReportsBoxOfficePage().isAtPage();
	}
}