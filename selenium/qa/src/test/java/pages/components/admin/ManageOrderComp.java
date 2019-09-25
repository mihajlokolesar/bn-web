package pages.components.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BaseComponent;
import utils.SeleniumUtils;

public class ManageOrderComp extends BaseComponent {

	private WebElement order;

	private String relativeTicketContainerXPath = ".//div[contains(@style,'min-height: 0px; height: auto; transition-duration: 300ms;')]";

	private String relativeTicketNumberList = "./div/div/div[2]";

	private String relativeOrderUserData = "./div/div/div[1]/p[2]";

	public ManageOrderComp(WebDriver driver, WebElement order) {
		super(driver);
		this.order = order;
	}

	public void openOrder() {
		waitForTime(1500);
		explicitWaitForVisibilityAndClickableWithClick(order);
		waitForTime(2000);
	}

	
	private ManageOrdersTicketRowComp findManageOrderTicketRow(String ticketNumber) {
		WebElement row = SeleniumUtils.getChildElementFromParentLocatedBy(order,
				By.xpath("//div//div[div[span[p[text()='" + ticketNumber + "']]]]"), driver);
		ManageOrdersTicketRowComp rowComponent = new ManageOrdersTicketRowComp(driver, row);
		return rowComponent;
	}

	public boolean isTicketRowPresent(String ticketNumber) {
		if (SeleniumUtils.refreshElement(order, driver)) {
			return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(order, relativeRowXpath(ticketNumber),
					driver);
		}
		return false;
	}

	private By relativeRowXpath(String ticketNumber) {
		return By.xpath("//div//div[div[span[p[text()='" + ticketNumber + "']]]]");
	}

	public String getFirstNameOfUser() {
		return getUserData()[1];
	}

	public String getLastNameOfUser() {
		return getUserData()[0];
	}

	private String[] getUserData() {
		WebElement element = SeleniumUtils.getChildElementFromParentLocatedBy(order, By.xpath(relativeOrderUserData),
				driver);
		String text = element.getText();
		return text.split(",");
	}
	
	public String selectTicketForRefund() {
		String[] ticketNumberList = getTicketList();
		ManageOrdersTicketRowComp row = findManageOrderTicketRow(ticketNumberList[0]);
		waitForTime(1500);
		row.clickOnCheckBox();
		return ticketNumberList[0];

	}


	private String[] getTicketList() {
		WebElement ele = SeleniumUtils.getChildElementFromParentLocatedBy(order, By.xpath(relativeTicketNumberList),
				driver);
		String[] listOfTickets = tokenizer("\\.", ele.getText());
		String[] escapedList = escapingTokens("#", listOfTickets);
		return escapedList;
	}
	
	private String[] tokenizer(String regEx, String content) {
		if (content != null && !content.isEmpty()) {
			String[] listOfTickets = content.split(regEx);
			if (listOfTickets.length == 1 && content.endsWith(regEx)) {
				String sub = content.substring(0, content.length() - 1);
				String[] a = { sub };
				listOfTickets = a;
			}
			return listOfTickets;
		}
		return null;
	}
	
	private String[] escapingTokens(String regEx, String[] content) {
		if (content != null && content.length > 0) {
			String[] escapedList = new String[content.length];
			for (int i = 0; i < content.length; i++) {
				String escaped = content[i].replace(regEx, "");
				escapedList[i] = escaped;
			}
			return escapedList;
		} else {
			return null;
		}
	}

}
