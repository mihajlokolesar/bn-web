package pages.components.dialogs;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class BoxOfficeSellCheckoutDialog extends DialogContainerComponent {

	// relative to dialog container element
	private String relativeTicketInfoRow = ".//div[div[div[p[contains(text(),'Ticket')]]]]";

	// relative to dialog container
	private String relativeRowsTicketInfo = relativeTicketInfoRow + "/div/div[div[p]]";
	// relative to dialog container
	private String relativeOrderTotal = relativeTicketInfoRow
			+ "//div[p[contains(text(),'Order total')]]/following-sibling::div";

	@FindBy(xpath = "//div/div/p[contains(text(),'Change tickets')]")
	private WebElement changeTicketLink;

	@FindBy(xpath = "//div//div//div[div[p[contains(text(),'cash')]]]")
	private WebElement payWithCashButton;

	@FindBy(id = "tendered")
	private WebElement cashTenederedField;

	// relative to dialog container
	private String relativeChangeDueElement = ".//p[contains(text(),'Change due')]/following-sibling::p";

	@FindBy(xpath = "//div//div//div[div[p[contains(text(),'credit')]]]")
	private WebElement payWithCreditCardButton;

	@FindBy(id = "firstName")
	private WebElement firstNameField;

	@FindBy(id = "lastName")
	private WebElement lastNameField;

	@FindBy(id = "email")
	private WebElement emailField;

	@FindBy(id = "phone")
	private WebElement phoneNumberField;

	@FindBy(id = "note")
	private WebElement orderNoteTextArea;

	@FindBy(xpath = "//div//button[span[contains(text(),'Complete order')]]")
	private WebElement completeOrderButton;

	public BoxOfficeSellCheckoutDialog(WebDriver driver) {
		super(driver);
	}

	public void clickOnChangeTicketLink() {
		waitForTime(1000);
		explicitWaitForVisibilityAndClickableWithClick(changeTicketLink);
		waitForTime(3000);
	}

	public void clickOnPayWithCash() {
		explicitWaitForVisibilityAndClickableWithClick(payWithCashButton);
	}

	public void clickOnPayWithCreditCard() {
		explicitWaitForVisibilityAndClickableWithClick(payWithCreditCardButton);
	}
	
	public void clickOnCompleteOrderButton() {
		explicitWaitForVisibilityAndClickableWithClick(completeOrderButton);
	}

	public void enterAmountToTenderedField(int amount) {
		SeleniumUtils.clearInputField(cashTenederedField, driver);
		waitVisibilityAndSendKeys(cashTenederedField, amount + "");
	}
	
	public void enterFirstName(String firstName) {
		waitVisibilityAndSendKeys(firstNameField, firstName);
	}
	
	public void enterLastName(String lastName) {
		waitVisibilityAndSendKeys(lastNameField, lastName);
	}
	
	public void enterEmailAddress(String email) {
		waitVisibilityAndSendKeys(emailField, email);
	}
	
	public void enterPhoneNumber(String phoneNumber) {
		waitVisibilityAndSendKeysSlow(phoneNumberField, phoneNumber);
	}
	
	public void enterOrderNote(String orderNode) {
		waitVisibilityAndSendKeys(orderNoteTextArea, orderNode);
	}

	public Double getOrderTotal() {
		return SeleniumUtils.getDoubleAmount(getDialogContainer(), relativeOrderTotal, driver);
	}

	public Double getChangeDueAmount() {
		return SeleniumUtils.getDoubleAmount(getDialogContainer(), relativeChangeDueElement, driver);
	}

	public List<CheckoutTicketInfoRow> findAllCheckoutTicketRowComponents() {
		List<WebElement> list = findAllCheckoutTicketRowElements();
		return list.stream().map(e -> new CheckoutTicketInfoRow(driver, e)).collect(Collectors.toList());
	}

	public CheckoutTicketInfoRow findCheckoutTicketRow(Predicate<CheckoutTicketInfoRow> predicate) {
		List<WebElement> list = findAllCheckoutTicketRowElements();
		Optional<CheckoutTicketInfoRow> optional = list.stream().map(e -> new CheckoutTicketInfoRow(driver, e))
				.filter(predicate).findFirst();
		return optional.isPresent() ? optional.get() : null;
	}

	private List<WebElement> findAllCheckoutTicketRowElements() {
		List<WebElement> elements = SeleniumUtils.getChildElementsFromParentLocatedBy(getDialogContainer(),
				By.xpath(relativeRowsTicketInfo), driver);
		return elements;
	}

}

class CheckoutTicketInfoRow extends BaseComponent {

	private WebElement row;

	// relative to row element
	private String relativeTicketAmountXpath = "./div[1]/p";

	// relative to row element
	private String relativeTicketPriceXpath = "./div[2]/p";

	// relative to row element
	private String relativeSubtotalXpath = "./div[3]/p";

	public CheckoutTicketInfoRow(WebDriver driver, WebElement row) {
		super(driver);
		this.row = row;
	}

	public String getTicketTypeName() {
		WebElement ticketAmountEl = SeleniumUtils.getChildElementFromParentLocatedBy(row,
				By.xpath(relativeTicketAmountXpath), driver);
		String text = ticketAmountEl.getText();
		String name = text.split("x")[1];
		return name.trim();
	}

	public int getTicketAmount() {
		WebElement ticketAmountEl = SeleniumUtils.getChildElementFromParentLocatedBy(row,
				By.xpath(relativeTicketAmountXpath), driver);
		String text = ticketAmountEl.getText();
		String total = text.split("x")[0];
		return Integer.parseInt(total.trim());
	}

	public int getTicketPrice() {
		return SeleniumUtils.getIntAmount(row, relativeTicketPriceXpath, driver);
	}

	public int getSubtotalPrice() {
		return SeleniumUtils.getIntAmount(row, relativeSubtotalXpath, driver);
	}

}
