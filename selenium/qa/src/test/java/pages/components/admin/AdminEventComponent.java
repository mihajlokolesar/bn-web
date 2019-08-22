package pages.components.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.Event;
import pages.BaseComponent;
import pages.components.dialogs.CancelEventDialog;
import pages.components.dialogs.DeleteEventDialog;
import utils.SeleniumUtils;

public class AdminEventComponent extends BaseComponent {
	
	private static final long serialVersionUID = 3498843919257980311L;

	// NOTE: all relative paths are relative to event field

	private WebElement event;

	private String relativeDropDownXpath = ".//button[@type='button']";

	private String relativeIsCanceledParagraphXpath = ".//div[p[contains(text(),'Cancelled')]]";

	private By dropDownCancelEvent = By
			.xpath("//body//div[@id='long-menu']//ul/li[div[span[contains(text(),'Cancel event')]]]");

	private By dropDownEditEvent = By
			.xpath("//body//div[@id='long-menu']//ul/li[div[span[contains(text(),'Edit event')]]]");

	private By dropDownDeleteEvent = By
			.xpath("//body//div[@id='long-menu']//ul/li[div[span[contains(text(),'Delete event')]]]");

	public AdminEventComponent(WebDriver driver, WebElement event) {
		super(driver);
		this.event = event;
	}

	public boolean isEventCanceled() {
		return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(event, By.xpath(relativeIsCanceledParagraphXpath),
				driver);
	}

	public void cancelEvent() {
		openDropDown();
		findActionAndClickInDropDown(dropDownCancelEvent);
		waitForTime(2000);
		new CancelEventDialog(driver).clickOnCancelEventButton();
	}

	public void deleteEvent(Event event) {
		openDropDown();
		findActionAndClickInDropDown(dropDownDeleteEvent);
		new DeleteEventDialog(driver).clickOnDeleteButton(event.getEventName());
	}
	
	public void editEvent(Event event) {
		openDropDown();
		findActionAndClickInDropDown(dropDownEditEvent);
	}
	
	

	private WebElement openDropDown() {
		WebElement dropDown = SeleniumUtils.getChildElementFromParentLocatedBy(event, By.xpath(relativeDropDownXpath),
				driver);
		explicitWaitForVisibilityAndClickableWithClick(dropDown);
		return dropDown;
	}

	private void findActionAndClickInDropDown(By by) {
		List<WebElement> elements = explicitWaitForVisiblityForAllElements(by);
		for (int i = elements.size() - 1; i >= 0; i--) {
			WebElement e = elements.get(i);
			if (isExplicitlyWaitVisible(e)) {
				explicitWaitForClickable(e);
				try {
					e.click();
					break;
				} catch (Exception exc) {
					System.out.println();
				}
			}
		}
	}

}
