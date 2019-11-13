package test.facade;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;

import test.facade.reports.ReportsBoxOfficeFacade;
import test.facade.reports.ReportsFacade;

/**
 * This has WebDriver, so it's not thread safe. It's just helper class to avoid
 * manual instantation of various facades.
 *
 */
public class FacadeProvider {

	private HashMap<Class, BaseFacadeSteps> facades;
	private WebDriver driver;

	public FacadeProvider(WebDriver driver) {
		this.driver = driver;
		this.facades = new HashMap<>();
	}

	public LoginStepsFacade getLoginFacade() {
		return (LoginStepsFacade) getFacade(LoginStepsFacade.class);
	}

	public AdminBoxOfficeFacade getBoxOfficeFacade() {
		return (AdminBoxOfficeFacade) getFacade(AdminBoxOfficeFacade.class);
	}

	public AdminEventDashboardFacade getEventDashboardFacade() {
		return (AdminEventDashboardFacade) getFacade(AdminEventDashboardFacade.class);
	}

	public AdminEventStepsFacade getAdminEventStepsFacade() {
		return (AdminEventStepsFacade) getFacade(AdminEventStepsFacade.class);
	}

	public AdminFanManagementFacade getFanManagmentFacade() {
		return (AdminFanManagementFacade) getFacade(AdminFanManagementFacade.class);
	}
	
	public EventStepsFacade getEventFacade() {
		return (EventStepsFacade) getFacade(EventStepsFacade.class);
	}
	
	public OrganizationStepsFacade getOrganizationFacade() {
		return (OrganizationStepsFacade) getFacade(OrganizationStepsFacade.class);
	}
	
	public ReportsFacade getReportsFacade() {
		return (ReportsFacade) getFacade(ReportsFacade.class);
	}
	
	public ReportsBoxOfficeFacade getReportsBoxOfficeFacade() {
		return (ReportsBoxOfficeFacade) getFacade(ReportsBoxOfficeFacade.class);
	}
	
	public VenueStepsFacade getVenueFacade() {
		return (VenueStepsFacade) getFacade(VenueStepsFacade.class);
	}

	private BaseFacadeSteps getFacade(Class clazz) {
		BaseFacadeSteps f = null;
		try {
			f = facades.get(clazz);
			if (f == null) {
				f = (BaseFacadeSteps) clazz.getConstructor(WebDriver.class).newInstance(driver);
				System.out.println("new: " + clazz.toString());
				facades.put(clazz, f);
			} else {
				System.out.println("existing: " + clazz.toString());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return f;
	}

}
