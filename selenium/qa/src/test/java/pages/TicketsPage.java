package pages;

import java.net.URISyntaxException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.SeleniumUtils;

public class TicketsPage extends BasePage {

	@FindBy(xpath = "//div/p[contains(text(),'Select tickets')]/following-sibling::button[.//span[contains(text(),'Continue')]]")
	private WebElement continueButton;
	
	@FindBy(xpath = "//div[@role='dialog']//div//button/span[contains(text(),'Already have an account?')]")
	private WebElement alreadyHaveAccountButton;
	
	@FindBy(xpath = "//div[@role='dialog']")
	private WebElement accountDialog;
	
	
	
	public List<WebElement> ticketTypes() {
		return explicitWait(15, ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
				"//div/p[contains(text(),'Select tickets')]/following-sibling::div//div[./p[contains(text(),'+')]]")));
	}
	
	public TicketsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {

	}

	public String getUrlPath() throws URISyntaxException {
		return SeleniumUtils.getUrlPath(driver);
	}

	public boolean verifyDifferentTicketTypesAreDisplayed() {
		List<WebElement> list = ticketTypes();
		if (list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public void addTicketForLastType() {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			List<WebElement> list = ticketTypes();
			incrementTicketNumber(list.get(list.size()-1));
		} else {
			throw new NotFoundException("No ticket types found");
		}
	}
	
	public void incrementTicketNumber(WebElement element) {
		explicitWait(20, ExpectedConditions.elementToBeClickable(element));
		element.click();
	}
	
	public void clickOnContinue() {
		explicitWait(20, ExpectedConditions.visibilityOf(continueButton));
		continueButton.click();
	}
	
	public void clickOnAlreadyHaveAnAccount() {
		explicitWait(15, ExpectedConditions.visibilityOf(accountDialog));
		explicitWait(10, ExpectedConditions.and(
				ExpectedConditions.visibilityOf(alreadyHaveAccountButton),
				ExpectedConditions.elementToBeClickable(alreadyHaveAccountButton)));
		alreadyHaveAccountButton.click();
	}

}
