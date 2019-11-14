package pages.admin.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pages.BasePage;
import utils.Constants;

public class ReportsMainPage extends BasePage {
	
	private final String TICKET_COUNTS_PARAM = "ticket-counts";
	private final String TRANSACTION_DETAILS_PARAM = "transaction-details";
	private final String BOX_OFFICE_SALES_PARAM = "box-office-sales-summary";
	private final String SETTLEMENT_PARAM = "settlement-list";
	public ReportsMainPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminReports());
	}
	
	public void clickOnTicketCounts() {
		WebElement el = getNavLinkElement(TICKET_COUNTS_PARAM);
		waitVisibilityAndBrowserCheckClick(el);
	}
	
	public void clickOnBoxOfficeSales() {
		WebElement el = getNavLinkElement(BOX_OFFICE_SALES_PARAM);
		waitVisibilityAndBrowserCheckClick(el);
	}
	
	private WebElement getNavLinkElement(String linkParam) {
		WebElement element = explicitWaitForVisibilityBy(By.xpath("//a[@href='/admin/reports/"+ linkParam  +"']"));
		return element;
	}

}
