package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;

public class TimeMenuDropDown extends BaseComponent {

	@FindBy(id = "time-menu")
	private WebElement timeMenu;

	public TimeMenuDropDown(WebDriver driver) {
		super(driver);
	}

	public void selectTime(WebElement element, String time) {
		if (time != null && !time.isEmpty()) {
			waitVisibilityAndBrowserCheckClick(element);
			explicitWaitForVisiblity(timeMenu);
			WebElement selectedTime = timeMenu.findElement(By.xpath(".//li[contains(text(),'" + time + "')]"));
			explicitWaitForVisiblity(selectedTime);
			explicitWaitForClickable(selectedTime);
			waitForTime(1000);
			waitVisibilityAndBrowserCheckClick(selectedTime);
			explicitWait(5, ExpectedConditions.attributeToBe(element, "value", time));
		}
	}
}
