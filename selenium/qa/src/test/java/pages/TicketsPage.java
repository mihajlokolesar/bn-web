package pages;

import java.net.URISyntaxException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.components.Header;
import utils.MsgConstants;
import utils.SeleniumUtils;

public class TicketsPage extends BasePage {

	@FindBy(xpath = "//div/p[contains(text(),'Select tickets')]/following-sibling::button[.//span[contains(text(),'Continue')]]")
	private WebElement continueButton;

	@FindBy(xpath = "//div[@role='dialog']//div//button[span[contains(text(),'Already have an account?')]]")
	private WebElement alreadyHaveAccountButton;

	@FindBy(xpath = "//div[@role='dialog']")
	private WebElement accountDialog;

	@FindBy(xpath = "//body//div[@role='dialog' and @aria-labelledby='dialog-title']//div/h1[contains(text(),'Login to your Big Neon account')]")
	private WebElement loginDialogTitle;

	public List<WebElement> addTicketTypes() {
		return explicitWait(15, ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
				"//div/p[contains(text(),'Select tickets')]/following-sibling::div//div[./p[contains(text(),'+')]]")));
	}

	public List<WebElement> removeTicketTypes() {
		return explicitWait(15, ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
				"//div/p[contains(text(),'Select tickets')]/following-sibling::div//div[./p[contains(text(),'-')]]")));
	}

	public TicketsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {

	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlMatches("tickets$"));
	}

	public void ticketsPageStepsWithLogin(String mail, String password, int numberOfTickets) {
		addNumberOfTickets(numberOfTickets);
		clickOnContinue();
		clickOnAlreadyHaveAnAccount();
		login(mail, password);
		waitForTime(1500);
		if (checkIfMoreEventsAreBeingPurchased()) {
			Header header = new Header(driver);
			header.clickOnShoppingBasket();
		}
	}

	public void ticketsPageStepsWithOutLogin(int numberOfTickets) {
		addNumberOfTickets(numberOfTickets);
		clickOnContinue();
		waitForTime(1000);

	}

	public String getUrlPath() throws URISyntaxException {
		return SeleniumUtils.getUrlPath(driver);
	}

	public void addNumberOfTickets(int number) {
		for (int k = 0; k < number; k++) {
			addTicketForLastType();
		}
	}

	public void removeNumberOfTickets(int number) {
		List<WebElement> list = removeTicketTypes();
		WebElement lastTicketType = list.get(list.size() - 1);
		WebElement currentQuantityEl = lastTicketType.findElement(By.xpath(".//following-sibling::p"));
		String text = currentQuantityEl.getText();
		Integer currentQuantity = Integer.parseInt(text);
		if (currentQuantity <= number && (currentQuantity - 1) > 0) {
			number = currentQuantity - 1;
		}
		for (int k = 0; k < number; k++) {
			removeTicketForLastType();
		}
	}

	private void removeTicketForLastType() {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			List<WebElement> list = removeTicketTypes();
			waitVisibilityAndClick(list.get(list.size() - 1));
		} else {
			throw new NotFoundException("No ticket types found");
		}
	}

	public void addTicketForLastType() {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			List<WebElement> list = addTicketTypes();
			incrementTicketNumber(list.get(list.size() - 1));
		} else {
			throw new NotFoundException("No ticket types found");
		}
	}

	public boolean verifyDifferentTicketTypesAreDisplayed() {
		List<WebElement> list = addTicketTypes();
		if (list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void incrementTicketNumber(WebElement element) {
		waitVisibilityAndClick(element);
	}

	public void clickOnContinue() {
		explicitWait(20, ExpectedConditions.visibilityOf(continueButton));
		continueButton.click();
	}

	public void clickOnAlreadyHaveAnAccount() {
		explicitWaitForVisiblity(accountDialog);
		explicitWaitForVisiblity(alreadyHaveAccountButton);
		explicitWaitForClickable(alreadyHaveAccountButton);
		waitForTime(1500);
		alreadyHaveAccountButton.click();
	}

	public void login(String username, String password) {
		explicitWaitForVisiblity(loginDialogTitle);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginWithoutNavigate(username, password);
	}

	private boolean checkIfMoreEventsAreBeingPurchased() {
		boolean retVal = isExplicitlyWaitVisible(5, message);
		if (retVal) {
			String msg = message.getText();
			if (msg != null && !msg.isEmpty() && msg.contains(MsgConstants.MORE_THAN_ONE_EVENT_PURCHASE_ERROR)) {
				retVal = true;
			}
		}
		return retVal;
	}
}