package test.facade.event.overview;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import model.Event;
import pages.admin.events.EventOverviewPage;
import test.facade.BaseFacadeSteps;

public class EventOverviewFacade extends BaseFacadeSteps {

	private EventOverviewPage eventOverviewPage;
	
	public EventOverviewFacade(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public void getAllEventOverviewInfo() {
		this.eventOverviewPage.getAllEventInfo();
	}
	
	public void whenUserComparesInfoOnOverviewWithGivenEvent(Event data, SoftAssert sa) {
		Event preview = getOverviewPage().getAllEventInfo();
		
	}
	
	public void setEventOverviewPage(String eventName) {
		this.eventOverviewPage = new EventOverviewPage(driver, eventName);
		if(!eventOverviewPage.isAtPage() || !eventOverviewPage.isTitleCorrect()) {
			throw new IllegalArgumentException("event name: " + eventName + "does not correspond to url");
		}
	}
	
	public EventOverviewPage getOverviewPage() {
		if(this.eventOverviewPage == null) {
			throw new RuntimeException("Event page not initialized");
		}
		return this.eventOverviewPage;
	}
	
	
	public boolean thenUserIsAtOverviewPage() {
		return getOverviewPage().isAtPage() && getOverviewPage().isTitleCorrect();
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
