package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.components.CreditCardDetailsFrame;
import utils.SeleniumUtils;

public class TicketsConfirmationPage extends BasePage {

	@FindBy(xpath = "//main//a[contains(@href,'tickets')]")
	public WebElement changeTicketLink;

	@FindBy(xpath = "//main//button[span[contains(text(),'Purchase tickets')]]")
	public WebElement purchaseTicketButton;

	@FindBy(xpath = "//header//span/a[contains(@href,'tickets/confirmation')]/button/span/span[1]")
	public WebElement shoppingBasket;

	@FindBy(xpath = "//main//form//iframe")
	private WebElement iframe;

	public TicketsConfirmationPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void presetUrl() {
	}

	public boolean isAtConfirmationPage() {
		return explicitWait(15, ExpectedConditions.urlContains("tickets/confirmation"));
	}

	public void shoppingBasketState() {
		try {
			explicitWait(15, ExpectedConditions.visibilityOf(shoppingBasket));
			String text = shoppingBasket.getText();
			System.out.println(text);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void enterCreditCardDetails() {
		String parentHandle = switchToFrame();
		CreditCardDetailsFrame creditCardDetailsFrame = new CreditCardDetailsFrame(driver);
		creditCardDetailsFrame.enterCreditCardNumber("4242424242424242");
		creditCardDetailsFrame.enterExpirationDate("0442");
		creditCardDetailsFrame.enterCvc("424");
		creditCardDetailsFrame.enterZip("24242");
		driver.switchTo().parentFrame();
	}

	public void clickOnPurchaseTicketButton() {
		explicitWait(10, ExpectedConditions.elementToBeClickable(purchaseTicketButton));
		purchaseTicketButton.click();
	}

	public String switchToFrame() {
		String parentHandle = driver.getWindowHandle();
		explicitWait(15, ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
		return parentHandle;
	}

}
