package pages.components.dialogs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.SeleniumUtils;

public class RefundDialog extends DialogContainerComponent {

	private String relativeRefundButtonXpath = ".//div//button[span[contains(text(),'Refund')]]";

	private String relativeRefundSuccesTitle = ".//h1[contains(text(),'Refund success')]";

	private String relativeClose = ".//button[span[contains(text(),'Close')]]";

	public RefundDialog(WebDriver driver) {
		super(driver);
	}

	public void clickOnDialogRefundButton() {
		waitForTime(1500);
		WebElement refundButton = SeleniumUtils.getChildElementFromParentLocatedBy(getDialogContainer(),
				By.xpath(relativeRefundButtonXpath), driver);
		explicitWaitForVisibilityAndClickableWithClick(refundButton);
	}

	public void confirmRefundIsSuccess() {
		//find some condition to wait for, to avoid this wait
		waitForTime(1500);
		if (isVisible() && isRefundSuccess()) {
			WebElement closeButton = SeleniumUtils.getChildElementFromParentLocatedBy(getDialogContainer(),
					By.xpath(relativeClose), driver);
			waitForTime(1000);
			explicitWaitForVisibilityAndClickableWithClick(closeButton);
		}
		
	}

	private boolean isRefundSuccess() {
		return SeleniumUtils.isChildElementVisibleFromParentLocatedBy(getDialogContainer(),
				By.xpath(relativeRefundSuccesTitle), driver);
	}
}
