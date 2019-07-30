package pages;

import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractBase {

	public WebDriver driver;

	public AbstractBase(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	@SuppressWarnings("unchecked")
	public <T, V> T explicitWait(int time, long poolingInterval, Function<? super WebDriver, V> condition)
			throws TimeoutException {
		return (T) new WebDriverWait(driver, time, poolingInterval).until(condition);
	}

	public <T, V> T explicitWait(int time, Function<? super WebDriver, V> condition) throws TimeoutException {
		return explicitWait(time, 500, condition);
	}

	@SuppressWarnings("unchecked")
	public <T, V> T explicitWaitNoPooling(int time, Function<? super WebDriver, V> condition) throws TimeoutException {
		return (T) new WebDriverWait(driver, time).until(condition);
	}

	public <T, V> T explicitWaitForVisiblity(WebElement element) {
		return explicitWait(15, ExpectedConditions.visibilityOf(element));
	}

	public <T, V> T explicitWaitForClickable(WebElement element) {
		return explicitWait(15, ExpectedConditions.elementToBeClickable(element));
	}

	public void waitForTime(int timeout, long poolingInterval) {
		try {
			new WebDriverWait(driver, timeout, poolingInterval)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("noelement")));
		} catch (Exception e) {
		}
	}

}
