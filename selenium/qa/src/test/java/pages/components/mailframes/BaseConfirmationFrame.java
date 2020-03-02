package pages.components.mailframes;

import java.math.BigDecimal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;
import utils.ProjectUtils;

public class BaseConfirmationFrame extends BaseComponent {
	
	@FindBy(xpath = "//tbody//tr/td[contains(text(),'Fees Total')]")
	private WebElement totalFees;

	@FindBy(xpath = "//tbody//tr/td[contains(text(),'Order Total')]")
	private WebElement orderTotal;

	public BaseConfirmationFrame(WebDriver driver) {
		super(driver);
	}
	
	public BigDecimal getTotalFees() {
		return getFees(totalFees);
	}

	public BigDecimal getOrderTotal() {
		return getFees(orderTotal);
	}
	
	private BigDecimal getFees(WebElement element) {
		explicitWaitForVisiblity(element);
		String totalFeesText = element.getText();
		return ProjectUtils.getBigDecimalMoneyAmount(totalFeesText.split(":")[1]);
	}

}
