package pages;

import java.net.URISyntaxException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import model.TicketType;
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
	
	private String ticketTypeContainerXpath = "//div/p[contains(text(),'Select tickets')]/following-sibling::div";

	private List<WebElement> getIncrementersForAllTicketTypes() {
		return explicitWait(15, ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
				"//div/p[contains(text(),'Select tickets')]/following-sibling::div//div[./p[contains(text(),'+')]]")));
	}

	private List<WebElement> getDecrementersForAllTicketTypes() {
		return explicitWait(15, ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
				"//div/p[contains(text(),'Select tickets')]/following-sibling::div//div[./p[contains(text(),'-')]]")));
	}
	
	private WebElement getIncreaseSpecificTicketTypeElement(String ticketTypeName) {
		return explicitWait(15,  ExpectedConditions.visibilityOfElementLocated(
				By.xpath(getXpathForOperatorForSpecificTicketType(ticketTypeName, "+"))));
	}
	
	private WebElement getDecreaseSpecificTicketTypeElement(String ticketTypeName) {
		return explicitWait(15,  ExpectedConditions.visibilityOfElementLocated(
				By.xpath(getXpathForOperatorForSpecificTicketType(ticketTypeName, "-"))));
	}
	
	private String getXpathForOperatorForSpecificTicketType(String ticketTypeName, String operator) {
		return ticketTypeContainerXpath + "//div[p[contains(text(),'" + ticketTypeName + "')]]/following-sibling::div//div[./p[contains(text(),'" + operator + "')]]";
	}

	public TicketsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {

	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlMatches("tickets$|tickets/$"));
	}
	
	public String getUrlPath() throws URISyntaxException {
		return SeleniumUtils.getUrlPath(driver);
	}
	
	public void addNumberOfTickets(int number, TicketType ticketType) {
		for (int k = 0; k< number; k++) {
			addTicketForTicketType(ticketType.getTicketTypeName());
		}
	}
	
	private void addTicketForTicketType(String ticketTypeName) {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			WebElement incrementer = getIncreaseSpecificTicketTypeElement(ticketTypeName);
			waitVisibilityAndBrowserCheckClick(incrementer);
		}
	}

	public void addNumberOfTickets(int number) {
		for (int k = 0; k < number; k++) {
			addTicketForLastType();
		}
	}
	
	private void addTicketForLastType() {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			List<WebElement> list = getIncrementersForAllTicketTypes();
			incrementTicketNumber(list.get(list.size() - 1));
		} else {
			throw new NotFoundException("No ticket types found");
		}
	}
	
	public void addNumberOfTicketsForEachType(int number) {
		for (int k = 0; k < number; k++) {
			addTicketForEachTicketType();
		}
	}
	
	private void addTicketForEachTicketType() {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			List<WebElement> list = getIncrementersForAllTicketTypes();
			for (WebElement incrementer : list) {
				incrementTicketNumber(incrementer);
			}
		}
	}
	
	public void checkIfTicketAreSelectedAndRemoveAllTickets() {
		List<WebElement> list = getDecrementersForAllTicketTypes();
		for (WebElement minus : list) {
			Integer qty = SeleniumUtils.getIntegerAmount(minus, By.xpath(".//following-sibling::p"), driver);
			if (qty.compareTo(new Integer(0)) > 0) {
				for (int i = qty; i > 0; i--) {
					waitVisibilityAndBrowserCheckClick(minus);
				}
			}
		}
	}

	public void removeNumberOfTickets(int number, TicketType ticketType) {
		WebElement decrementer = getDecreaseSpecificTicketTypeElement(ticketType.getTicketTypeName());
		Integer qty = SeleniumUtils.getIntegerAmount(decrementer, By.xpath(".//following-sibling::p"), driver);
		number = calculateRemoveNumber(qty, number);
		for (int k = 0; k < number; k++) {
			removeTicketForTicketType(ticketType.getTicketTypeName());
		}
	}

	public void removeNumberOfTickets(int number) {
		List<WebElement> list = getDecrementersForAllTicketTypes();
		WebElement lastTicketType = list.get(list.size() - 1);
		WebElement currentQuantityEl = lastTicketType.findElement(By.xpath(".//following-sibling::p"));
		String text = currentQuantityEl.getText();
		Integer currentQuantity = Integer.parseInt(text);
		number = calculateRemoveNumber(currentQuantity, number);
		for (int k = 0; k < number; k++) {
			removeTicketForLastType();
		}
	}
	
	private int calculateRemoveNumber(Integer currentQuantity, int intendedRemoveNumber) {
		if (currentQuantity <= intendedRemoveNumber && (currentQuantity - 1) > 0) {
			intendedRemoveNumber = currentQuantity - 1;
		} else if (currentQuantity.equals(1)) {
			intendedRemoveNumber = 0;
		}
		return intendedRemoveNumber;
	}
	
	private void removeTicketForLastType() {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			List<WebElement> list = getDecrementersForAllTicketTypes();
			waitVisibilityAndBrowserCheckClick(list.get(list.size() - 1));
		} else {
			throw new NotFoundException("No ticket types found");
		}
	}
	
	private void removeTicketForTicketType(String ticketTypeName) {
		if (verifyDifferentTicketTypesAreDisplayed()) {
			WebElement decrementer = getDecreaseSpecificTicketTypeElement(ticketTypeName);
			waitVisibilityAndBrowserCheckClick(decrementer);
		}
	}
		
	private boolean verifyDifferentTicketTypesAreDisplayed() {
		List<WebElement> list = getIncrementersForAllTicketTypes();
		if (list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private void incrementTicketNumber(WebElement element) {
		waitVisibilityAndBrowserCheckClick(element);
	}

	public void clickOnContinue() {
		explicitWait(20, ExpectedConditions.visibilityOf(continueButton));
		waitVisibilityAndBrowserCheckClick(continueButton);
	}

	public void clickOnAlreadyHaveAnAccount() {
		explicitWaitForVisiblity(accountDialog);
		explicitWaitForVisiblity(alreadyHaveAccountButton);
		explicitWaitForClickable(alreadyHaveAccountButton);
		waitForTime(1500);
		waitVisibilityAndBrowserCheckClick(alreadyHaveAccountButton);
	}

	public void login(String username, String password) {
		explicitWaitForVisiblity(loginDialogTitle);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginWithoutNavigate(username, password);
	}

	public boolean checkIfMoreEventsAreBeingPurchased() {
		return isNotificationDisplayedWithMessage(MsgConstants.MORE_THAN_ONE_EVENT_PURCHASE_ERROR, 4);
	}
}