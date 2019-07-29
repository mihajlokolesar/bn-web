package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public abstract class BasePage extends AbstractBase {

	
	private String url;

	public BasePage(WebDriver driver) {
		super(driver);
		presetUrl();
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

}
