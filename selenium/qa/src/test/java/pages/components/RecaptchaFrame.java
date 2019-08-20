package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;

public class RecaptchaFrame extends BaseComponent{
	
	@FindBy(xpath = "//div[@class='g-recaptcha']//iframe[contains(@src,'google')]")
	private WebElement recaptchaIframe;

	@FindBy(id = "recaptcha-anchor")
	private WebElement recaptchaAnchorInFrame;

	public RecaptchaFrame(WebDriver driver) {
		super(driver);
	}
	
	public void clickOnRecaptcha() {
		WebElement recaptcha = driver.findElement(By.className("g-recaptcha"));
		waitVisibilityAndClick(recaptcha);
		explicitWait(10, ExpectedConditions.frameToBeAvailableAndSwitchToIt(recaptchaIframe));
		explicitWait(10, ExpectedConditions.attributeContains(recaptchaAnchorInFrame, "aria-checked", "true"));
		driver.switchTo().parentFrame();
	}

	
}
