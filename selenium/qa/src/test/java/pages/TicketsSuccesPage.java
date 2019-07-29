package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TicketsSuccesPage extends BasePage{
	
	@FindBy(id = "phone")
	private WebElement mobileNumberField;

	@FindBy(xpath = "//main//form//button[@type='submit']")
	private WebElement sendMeTextButton;
	public TicketsSuccesPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
	}

}
