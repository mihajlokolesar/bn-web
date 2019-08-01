package pages;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.BrowsersEnum;
import config.DriverFactory;

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

	public void waitVisibilityAndClick(WebElement element) {
		explicitWait(15, ExpectedConditions.and(ExpectedConditions.visibilityOf(element),
				ExpectedConditions.elementToBeClickable(element)));
		element.click();
	}

	public void waitVisibilityAndSendKeys(WebElement element, String value) {
		explicitWaitForVisiblity(element);
		explicitWaitForClickable(element);
		element.sendKeys(value);
	}

	public void waitForTime(int timeout, long poolingInterval) {
		try {
			new WebDriverWait(driver, timeout, poolingInterval)
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("noelement")));
		} catch (Exception e) {
		}
	}

	public void waitForTime(long mills) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(mills, TimeUnit.MILLISECONDS)
					.pollingEvery(mills, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class)
					.ignoring(NotFoundException.class).ignoring(Exception.class);
			wait.until(new Function<WebDriver, WebElement>() {

				@Override
				public WebElement apply(WebDriver t) {
					return driver.findElement(By.id("noelement"));
				}

			});
		} catch (Exception e) {
		}
	}

	private boolean isSafari() {
		BrowsersEnum browser = DriverFactory.getBrowser();
		if (BrowsersEnum.REMOTE.equals(browser)) {
			String b = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
			if (b.contains("safari")) {
				return true;
			} else {
				return false;
			}
		} else if (BrowsersEnum.SAFARI.equals(browser)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * NOTE: SAFARI DOES NOT SUPPORT Advanced Interaction API
	 * 
	 * @param text
	 * @return
	 */
	public Actions actionsManualType(String text) {
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.chord(text)).perform();
		return actions;
	}

	public Actions actionsMoveToElement(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
		return actions;
	}

}
