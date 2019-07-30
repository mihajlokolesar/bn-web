package pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import pages.BasePage;
import utils.Constants;

public class AdminEventsPage extends BasePage {

	public AdminEventsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminEvents());
	}

}
