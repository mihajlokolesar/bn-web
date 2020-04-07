package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BaseComponent;

public class PaginationComponent extends BaseComponent {

	@FindBy(xpath = "//main//div[div[p[text()='Previous']] and div[2]/div[1]/p[text()='1'] and div[p[text()='Next']]]")
	private WebElement container;


	private String relativePreviousXpath = "./div[p[contains(text(),'Previous')]]";

	private String relativeNextXpath = "./div[p[contains(text(),'Next')]]";

	private final String PREVIOUS = "Previous";
	private final String NEXT = "Next";


	public PaginationComponent(WebDriver driver) {
		super(driver);
	}

	public boolean navigateNext(Integer pageIndex){
		WebElement next = getNavigationElement(NEXT);
		if (next != null && isNavigationElementValid(next) ) {
			waitVisibilityAndBrowserCheckClick(next);
			Integer newIndex = getPageIndex();
			if( newIndex > pageIndex) {
				pageIndex = newIndex;
				return true;
			} else {
				return  false;
			}
		} else {
			return false;
		}
	}

	public Integer getPageIndex(){
		String url = driver.getCurrentUrl();
		boolean isPageQueryPresent = isExplicitConditionTrue(5, ExpectedConditions.urlContains("page"));
		if (isPageQueryPresent) {
			String page = url.split("page")[1];
			page = page.replace("=","");
			Integer intPage = Integer.parseInt(page);
			return intPage;
		} else {
			return -1;
		}
	}


	public boolean isNavigationElementValid(WebElement navElement) {
		WebElement imgEl = getAccessUtils().getChildElementFromParentLocatedBy(navElement, By.xpath("./img"));
		if (imgEl != null) {
			String display = imgEl.getAttribute("display");
			return !"none".equals(display);
		} else {
			return false;
		}
	}

	private WebElement getNavigationElement(String direction) {
		By by = null;
		if (PREVIOUS.equals(direction)) {
			by = By.xpath(relativePreviousXpath);
		} else if (NEXT.equals(direction)) {
			by = By.xpath(relativeNextXpath);
		}
		return getAccessUtils().getChildElementFromParentLocatedBy(container, by);
	}




}
