package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class PaginationComponent extends BaseComponent{

	@FindBy(xpath = "//main//div[div[p[text()='Previous']] and div[2]/div[1]/p[text()='1'] and div[p[text()='Next']]]")
	private WebElement container;

	
	public PaginationComponent(WebDriver driver) {
		super(driver);
	}
}
