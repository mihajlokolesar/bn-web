package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.components.Header;

public abstract class BasePage extends AbstractBase {
	
	private Header header;

	@FindBy(id = "message-id")
	public WebElement message;
	
	private String url;

	public BasePage(WebDriver driver) {
		super(driver);
		presetUrl();
		this.header = new Header(driver);
	}

	public abstract void presetUrl();
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean isAtPage() {
		return explicitWait(10, ExpectedConditions.urlToBe(getUrl()));
	}
	
	public void logOut() {
		header.logOut();
	}
	
	public Header getHeader() {
		return this.header;
	}
	
	public boolean isNotificationDisplayedWithMessage(String textOfMessage) {
		explicitWait(10, ExpectedConditions.visibilityOf(message));
		String msg = message.getText();
		if (msg != null && !msg.isEmpty() && msg.contains(textOfMessage)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNotificationDisplayedWithMessage(String[] messages) {
		explicitWait(10, ExpectedConditions.visibilityOf(message));
		String msg = message.getText();
		for(String s : messages) {
			if (msg != null && !msg.isEmpty() && msg.contains(s)) {
				return true;
			}
		}
		return false;
	}

}
