package pages.components.dialogs;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeleteEventDialog extends DialogContainerComponent {

	@FindBy(xpath = "//div/h1[contains(text(),'Delete Event')]")
	private WebElement title;

	@FindBy(xpath = "//form//button[@type='submit' and span[contains(text(),'Delete event')]]")
	private WebElement deleteEventButton;

	@FindBy(xpath = "//form//button[@type='button' and span[contains(text(),'Keep event')]]")
	private WebElement keepEventButton;

	public DeleteEventDialog(WebDriver driver) {
		super(driver);
	}

	public boolean isCorrectEvent(String eventName) {
		return isExplicitlyWaitVisible(5, By.xpath("//form//div//p[contains(text(),'" + eventName + "')]"));
	}

	public void clickOnDeleteButton(String eventName) {
		if (isCorrectEvent(eventName)) {
			explicitWaitForVisibilityAndClickableWithClick(deleteEventButton);
		} else {
			throw new NoSuchElementException("Event name on delete dialog is not same as: " + eventName);
		}
	}
	
	public void clickOnKeepEvent() {
		explicitWaitForVisibilityAndClickableWithClick(keepEventButton);
	}
}
