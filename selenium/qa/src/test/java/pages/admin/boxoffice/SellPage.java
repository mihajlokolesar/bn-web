package pages.admin.boxoffice;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import utils.Constants;

public class SellPage extends BasePage {

	public SellPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getBoxOfficeSell());
	}
	

}
