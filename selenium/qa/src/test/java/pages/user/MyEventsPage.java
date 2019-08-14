package pages.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.User;
import pages.BasePage;
import pages.components.UserSideNavBar;
import pages.components.user.EventComponent;
import utils.Constants;
import utils.SeleniumUtils;

public class MyEventsPage extends BasePage {

	private UserSideNavBar userSideNavBar;

	@FindBy(xpath = "//body//main//div//a[span[contains(text(),'Upcoming')]]")
	private WebElement upcomingEventsLink;

	@FindBy(xpath = "//body//main//div//a[span[contains(text(),'Past')]]")
	private WebElement pastEventsLink;

	private String listOfEventsContainersXpath = "//body//main//div[div[div[div[div[p[contains(text(),'Tickets')]]]]]]";

	private String selectedEventXpath = "//body//main//div[div[contains(@style,'height: auto; transition-duration: 300ms;')]]";

	private String relativeViewMyTicketsLink = ".//div//a[span[contains(text(),'View my tickets')]]";

	private List<WebElement> listOfEvents;

	@FindBy(id = "emailOrCellNumber-error-text")
	private WebElement emailOrCellNumberError;

	@FindBy(id = "emailOrCellNumber")
	private WebElement emailOrPhoneInputField;

	@FindBy(xpath = "//body//div[@role='dialog' and @aria-labelledby='dialog-title']//div//button[span[contains(text(),'Send tickets')]]")
	private WebElement sendTicketsButton;

	@FindBy(xpath = "//body//div[@role='dialog' and @aria-labelledby='dialog-title']//div//button[span[contains(text(),'Cancel')]]")
	private WebElement cancelButton;

	public MyEventsPage(WebDriver driver) {
		super(driver);
		userSideNavBar = new UserSideNavBar(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getMyEventsBigNeon());
	}

	public EventComponent getSelectedEvent() {
		WebElement event = getSelectedEventTicketListContainer();
		return new EventComponent(driver, event);
	}

	private WebElement getSelectedEventTicketListContainer() {
		waitForTime(1500);
		WebElement element = explicitWait(15,
				ExpectedConditions.visibilityOfElementLocated(By.xpath(selectedEventXpath)));
		return element;
	}
	/**
	 * Looks through events that are named with param eventName, and in each event it looks for ticket
	 * with ticketNumber and orderNumber
	 * @param ticketNumber
	 * @param orderNumber
	 * @param eventName
	 * @return
	 */
	public boolean checkIfTicketExists(String ticketNumber, String orderNumber, String eventName) {
		List<WebElement> events = explicitWait(15, ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath("//body//main//div[div[div[div[p[text()='" + eventName + "']]]]]")));

		for (WebElement event : events) {
			EventComponent component = clickOnViewMyTicketOfEvent(event);
			try {
				boolean retVal = component.isTicketNumberPresent(ticketNumber);
				retVal = retVal && component.isOrderNumberPresent(orderNumber);
				if (retVal) {
					return retVal;
				}
			} catch (NoSuchElementException e) {
				continue;
			}

		}
		return false;
	}

	public WebElement findEventByName(String eventName) {
		return explicitWait(15, ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//body//main//div[div[div[div[p[text()='" + eventName + "']]]]]")));
	}

	public EventComponent clickOnViewMyTicketOfEvent(WebElement eventParent) {
		clickOnEventViewMyTickets(eventParent);
		return getSelectedEvent();
	}

	private void clickOnEventViewMyTickets(WebElement eventElement) {
		WebElement viewMyTicketsButton = SeleniumUtils.getChildElementFromParentLocatedBy(eventElement,
				By.xpath(relativeViewMyTicketsLink), driver);
		explicitWaitForVisibilityAndClickableWithClick(viewMyTicketsButton);
	}

	public String getEventName(WebElement event) {
		WebElement eventName = event.findElement(By.xpath(".//div/div/div/div/p[2]"));
		return eventName.getText();
	}

	public EventComponent findEventWithTransferableTickets() {
		if (!isEventsPresent()) {
			throw new NotFoundException("Upcoming events not found");
		}
		for (WebElement e : listOfEvents) {
			WebElement viewMyTicketsEl = e.findElement(By.xpath(relativeViewMyTicketsLink));
			explicitWaitForVisibilityAndClickableWithClick(viewMyTicketsEl);
			String eventName = getEventName(e);
			EventComponent eventComponent = getSelectedEvent();
			WebElement row = eventComponent.selectTransferableRow();
			if (row != null) {
				eventComponent.setEventName(eventName);
				return eventComponent;
			}
		}
		return null;
	}

	public WebElement clickOnFirstOneViewMyTicketsButton() {
		if (!isEventsPresent()) {
			throw new NotFoundException("Upcoming events not found");
		}
		WebElement viewMyTicketsEl = listOfEvents.get(0).findElement(By.xpath(relativeViewMyTicketsLink));
		explicitWaitForVisibilityAndClickableWithClick(viewMyTicketsEl);
		return listOfEvents.get(0);
	}

	public void enterReceiversMail(User receiver) {
		waitVisibilityAndSendKeys(emailOrPhoneInputField, receiver.getEmailAddress());
		waitVisibilityAndClick(sendTicketsButton);
	}

	private boolean isEventsPresent() {
		listOfEvents = explicitWait(15,
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(listOfEventsContainersXpath)));
		if (listOfEvents == null || listOfEvents.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

}
