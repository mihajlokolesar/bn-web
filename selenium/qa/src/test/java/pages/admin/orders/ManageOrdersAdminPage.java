package pages.admin.orders;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BasePage;
import pages.components.admin.ManageOrderComp;
import utils.Constants;
import utils.SeleniumUtils;
/**
 * Currently this page is for Refund ticket that was under Tools - > Manage Orders menu steps, if that is returned 
 * we can bring it back
 * @author simpletask
 *
 */
public class ManageOrdersAdminPage extends BasePage {

	@FindBy(xpath = "//body//main//div[div[div[div[div[p[contains(text(),'Manage orders')]]]]]]")
	private WebElement mainContentContainer;

	@FindBy(xpath = "//body//main//button[not(@disabled='') and span[contains(text(),'Refund')]]")
	private WebElement refundButton;

	private String relativeTicketUserData = ".//p[text()='1']/following-sibling::p";

	public ManageOrdersAdminPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
	}

	@Override
	public boolean isAtPage() {
		return explicitWait(15, ExpectedConditions.urlMatches(Constants.getAdminEvents() + "/*.*/manage-orders"));
	}

	public void clickOnRefundButton() {
		explicitWaitForVisibilityAndClickableWithClick(refundButton);
	}

	public ManageOrderComp openOrderWithIndexNumber(int rowNumber) {
		waitForTime(2000);
		ManageOrderComp orderComponent = findOrderWithIndexNumber(rowNumber);
		orderComponent.openOrder();
		return orderComponent;
	}

	public boolean isTicketPresentInOrder(String ticketNumber, String firstName, String lastName) {
		if (isOrderWithUserPresent(firstName, lastName)) {
			ManageOrderComp orderComponent = null;
			WebElement order = findOrderWithUser(firstName, lastName);
			orderComponent = new ManageOrderComp(driver, order);
			orderComponent.openOrder();
			return orderComponent.isTicketRowPresent(ticketNumber);
		}
		return false;

	}

	public ManageOrderComp findOrderWithIndexNumber(int rowNumber) {
		WebElement order = SeleniumUtils.getChildElementFromParentLocatedBy(mainContentContainer,
				By.xpath("./div[2]/div/div[//p[text()='" + rowNumber + "']]"), driver);
		ManageOrderComp comp = new ManageOrderComp(driver, order);
		return comp;
	}

	public boolean isOrderWithUserPresent(String firstName, String lastName) {
		return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(mainContentContainer,
				findOrderByUserXpath(firstName, lastName), driver);
	}

	public WebElement findOrderWithUser(String firstName, String lastName) {
		WebElement element = SeleniumUtils.getChildElementFromParentLocatedBy(mainContentContainer,
				findOrderByUserXpath(firstName, lastName), driver);
		return element;
	}

	private By findOrderByUserXpath(String firstName, String lastName) {
		return By.xpath("./div[2]/div/div[//p[contains(text(),'" + lastName.trim() + ", " + firstName.trim() + "')]]");
	}

}
