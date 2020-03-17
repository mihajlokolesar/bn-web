package pages.components.chatboot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;

public class ChatBotFrame extends BaseComponent {
	
	@FindBy(id = "drift-widget-container")
	private WebElement frameContainer;
	
	@FindBy(id = "drift-widget")
	private WebElement frameElement;
	
	@FindBy(id = "widgetCloseButton")
	private WebElement closeBot;

	public ChatBotFrame(WebDriver driver) {
		super(driver);
	}
	
	public void closeChatBot() {
		switchToFrame();
		waitForTime(1000);
		waitVisibilityAndBrowserCheckClick(closeBot);
		driver.switchTo().parentFrame();
		waitForTime(1000);
	}
	
	public boolean isChatBotOpened() {
		waitForTime(1000);
		return isExplicitlyWaitVisible(15, frameContainer);
	}
	
	public void switchToFrame() {
		explicitWait(15, ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}

}
