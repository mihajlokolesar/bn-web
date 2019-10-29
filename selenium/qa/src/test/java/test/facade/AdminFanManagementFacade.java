package test.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import model.Event;
import model.User;
import pages.admin.fans.AdminFanProfilePage;
import pages.admin.fans.AdminFanProfilePage.FanProfileEventDataHolder;
import pages.admin.fans.AdminFansListPage;
import pages.admin.fans.AdminFansListPage.FanRowComponent;
import pages.admin.orders.manage.SelectedOrderPage;
import pages.components.admin.AdminSideBar;
import pages.components.admin.fans.manage.FanProfileEventSummaryComponent;
import pages.components.admin.orders.manage.ActivityItem;
import pages.components.admin.orders.manage.ActivityItem.RefundedExpandedContent;

public class AdminFanManagementFacade extends BaseFacadeSteps {

	private AdminSideBar adminSideBar;
	private AdminFansListPage fansListPage;
	private Map<String, Object> dataMap;

	private Integer FAN_PROFILE_EVENT_LIST_LIMIT = 30;
	private Integer FAN_PROFILE_EVENT_LIST_LIMIT_LG = 100;
	private String FAN_PROFILE_PAGE_KEY = "fan_profile_page";
	private String FAN_PROFILE_UPCOMING_EVENT_LIST = "fan_profile_upcoming_event_list";
	private String FAN_PROFILE_REFUND_SUMMARY_CARD = "fan_profile_refund_summary_card";

	public AdminFanManagementFacade(WebDriver driver) {
		super(driver);
		this.adminSideBar = new AdminSideBar(driver);
		this.fansListPage = new AdminFansListPage(driver);
		this.dataMap = new HashMap<String, Object>();
	}

	public void givenUserIsOnFansPage() {
		this.adminSideBar.clickOnFansPage();
		fansListPage.isAtPage();
	}

	public void whenUserSelectsFanFormList(User fan) {
		FanRowComponent rowComponent = fansListPage.findFanRowByName(fan.getFirstName());
		String fanId = rowComponent.getFanId();
		rowComponent.clickOnRow();
		AdminFanProfilePage profilePage = new AdminFanProfilePage(driver, fanId);
		profilePage.isAtPage();
		setData(FAN_PROFILE_PAGE_KEY, profilePage);
	}

	public boolean whenUserChecksValidityOfFanInformation(User fan) {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);
		boolean retVal = true;
		retVal = retVal && profilePage.getFanName().equals(fan.getFirstName() + " " + fan.getLastName());
		retVal = retVal && profilePage.getFanEmail().equals(fan.getEmailAddress());
		return retVal;
	}

	public void whenUserChecksUpcomingEventList() {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);
		List<FanProfileEventDataHolder> upcomingEvents = profilePage.getEventsData(FAN_PROFILE_EVENT_LIST_LIMIT);
		setData(FAN_PROFILE_UPCOMING_EVENT_LIST, upcomingEvents);
	}

	public boolean whenUserClicksOnPastEventsLink() {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);
		profilePage.clickOnPastEvents();
		boolean isOnPastEventsPage = profilePage.isOnPastEventsPage();
		return isOnPastEventsPage;
	}

	public boolean thenUserComparesUpcomingAndPastEventLists() {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);

		List<FanProfileEventDataHolder> upcomingEvents = 
				(List<FanProfileEventDataHolder>) getData(FAN_PROFILE_UPCOMING_EVENT_LIST);
		List<FanProfileEventDataHolder> pastEvents = profilePage.getEventsData(FAN_PROFILE_EVENT_LIST_LIMIT);

		if (upcomingEvents.size() == pastEvents.size()) {
			return !pastEvents.containsAll(upcomingEvents);
		} else if (upcomingEvents.size() > pastEvents.size()) {
			return !upcomingEvents.containsAll(pastEvents);
		} else {
			return !pastEvents.containsAll(upcomingEvents);
		}
	}
	
	public boolean thenEventSummaryDataShouldBePresentAndActivitiesCollapsed() {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);
		List<WebElement> listOfEvents = profilePage.findEvents(FAN_PROFILE_EVENT_LIST_LIMIT_LG);
		return listOfEvents
				.stream()
				.map(el -> new FanProfileEventSummaryComponent(driver, el))
				.allMatch(summary -> !summary.getEventName().isEmpty() && !summary.getVenueInfo().isEmpty()
						&& (summary.getEventDateAndTime() != null) && (summary.getShowDetailsButtonElement() != null));
	}
	
	public ActivityItem whenUserClicksOnShowDetailsOfSelectedSummaryCard(Event event) {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);
		FanProfileEventSummaryComponent summaryCard = profilePage
				.findSummaryComponent(comp->comp.getEventName().contains(event.getEventName()),FAN_PROFILE_EVENT_LIST_LIMIT);
		if (summaryCard != null) {
			summaryCard.clickOnShowDetailsButton();
			ActivityItem activityItem = summaryCard.getActivityItem(aitem->aitem.isPruchased());
			if(activityItem != null) {
				activityItem.clickOnShowDetailsLink();
				return activityItem;
			}
		} 
		return null;
	}
	
	public void whenUserPicksEventSummaryCard(Event event) {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);
		FanProfileEventSummaryComponent summaryCard = profilePage.findSummaryComponent(
				comp-> comp.getEventName().contains(event.getEventName()), FAN_PROFILE_EVENT_LIST_LIMIT);
		if(summaryCard == null) {
			throw new NoSuchElementException("Summary card on fan profile page not found");
		}
		setData(FAN_PROFILE_REFUND_SUMMARY_CARD	, summaryCard);
	}
	
	public SelectedOrderPage whenUserClicksOnPurchasedActivityItemOrderNumberLink() {
		FanProfileEventSummaryComponent summaryCard = (FanProfileEventSummaryComponent) getData(FAN_PROFILE_REFUND_SUMMARY_CARD);
		summaryCard.clickOnShowDetailsButton();
		ActivityItem activityItem = summaryCard.getActivityItem(aitem->aitem.isPruchased());
		if (activityItem != null) {
			String orderId = activityItem.getOrderId();
			activityItem.clickOnOrderNumberLink();
			SelectedOrderPage selectedOrderPage = new SelectedOrderPage(driver, orderId);
			return selectedOrderPage;
		} else {
			return null;
		}
	}
	
	public boolean thenRefundedActivityItemForSpecificEventShouldBeVisible(User refunder, User refundee) {
		FanProfileEventSummaryComponent summaryCard = (FanProfileEventSummaryComponent) getData(FAN_PROFILE_REFUND_SUMMARY_CARD);
		summaryCard.clickOnShowDetailsButton();
		ActivityItem activityItem = summaryCard.getActivityItem(aitem->aitem.isRefunded());
		if (activityItem != null) {
			if(activityItem.isRefundedItemRowInfoValid(refunder, refundee)) {
				activityItem.clickOnShowDetailsLink();
				RefundedExpandedContent expandedContent = activityItem.getRefundedExpandedContent();
				return expandedContent.isDataVisible();
			}
			
		}
		return false;
	}
			
	public boolean thenUserShoudBeOnFanProfilePage() {
		AdminFanProfilePage profilePage = (AdminFanProfilePage) getData(FAN_PROFILE_PAGE_KEY);
		return profilePage.isAtPage();
	}

	protected void setData(String key, Object value) {
		this.dataMap.put(key, value);
	}

	protected Object getData(String key) {
		return dataMap.get(key);
	}

}
