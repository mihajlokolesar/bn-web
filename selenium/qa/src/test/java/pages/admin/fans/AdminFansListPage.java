package pages.admin.fans;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;
import pages.BasePage;
import utils.Constants;
import utils.SeleniumUtils;

public class AdminFansListPage extends BasePage {
	
	private String containerXpath = "//main//div[div[div[div[div[input[contains(@name,'Search')]]]]]]";

	public AdminFansListPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void presetUrl() {
		setUrl(Constants.getAdminFans());		
	}
	
	public FanRowComponent findFanRowByName(String userName) {
		WebElement row = findFanRowElementByName(userName);
		FanRowComponent rowComponent = new FanRowComponent(driver, row);
		return rowComponent;
	}
	
	private WebElement findFanRowElementByName(String name) {
		WebElement row= explicitWait(15, ExpectedConditions.visibilityOfElementLocated(
				By.xpath(containerXpath + "//a[div[span[div[p[contains(text(),'" + name + "')]]]]]")));
		return row;
	}
	
	private List<WebElement> findAllFansElements(){
		List<WebElement> list = explicitWaitForVisiblityForAllElements(By.xpath(containerXpath + "//a"));
		return list;
	}
	
	
	public class FanRowComponent extends BaseComponent {
		
		private WebElement container;

		private String relativeNameXpath = "./div/span/div/p";
		
		private String relativeEmailXpath = "./div/span[2]/p";
		
		private String relativeLastOrderDateXpath = "./div/span[3]/p";
		
		private String relativeOrdersXpath = "./div/span[4]/p";
		
		private String relativeRevenueXpath = "./div/span[5]/p";
		
		private String relativeDateAddedXpath = "./div/span[6]/p";
		
		public FanRowComponent(WebDriver driver, WebElement container) {
			super(driver);
			this.container = container;
		}
		
		public void clickOnRow() {
			waitVisibilityAndBrowserCheckClick(container.findElement(By.xpath("./div")));
		}
		
		public String getFanId() {
			String href = container.getAttribute("href");
			int lastIndex = href.lastIndexOf("/");
			return href.substring(lastIndex+1);
		}
		
		public String getName() {
			String text = getNameElement().getText();
			return text;
		}
		
		public String getEmail() {
			String text = getEmailElement().getText();
			return text;
		}
		
		private WebElement getEmailElement() {
			return SeleniumUtils.getChildElementFromParentLocatedBy(container, 
					By.xpath(relativeEmailXpath), driver);
		}
		
		private WebElement getNameElement() {
			return SeleniumUtils.getChildElementFromParentLocatedBy(container,
					By.xpath(relativeNameXpath), driver);
		}
				
	}
	
}