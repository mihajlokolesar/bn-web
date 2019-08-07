package pages.admin.boxoffice;

import org.openqa.selenium.WebDriver;

import pages.BasePage;
import utils.Constants;

public class SellPage extends BasePage {

	public SellPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getBoxOfficeSell());
	}

}
