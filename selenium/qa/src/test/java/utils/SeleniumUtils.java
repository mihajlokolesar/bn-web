package utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtils {

	public static WebDriver switchToFrame(WebDriver driver, WebElement iframe) {
		driver.switchTo().frame(iframe);
		return driver;
	}

	public static WebDriver switchToParentWindow(String parentHandle, WebDriver driver) {
		String currentHandle = driver.getWindowHandle();
		if (!currentHandle.equalsIgnoreCase(parentHandle)) {
			driver.switchTo().window(parentHandle);
		}
		return driver;
	}

	public static WebDriver switchWindow(String currentHandle, WebDriver driver) {
		List<String> handles = new ArrayList<String>(driver.getWindowHandles());
		for (String h : handles) {
			if (!currentHandle.equalsIgnoreCase(h)) {
				driver.switchTo().window(h);
			}
		}
		return driver;
	}

	public static WebDriver switchToChildWindow(String parentWindow, WebDriver driver) {
		String current = driver.getWindowHandle();
		if (current.equalsIgnoreCase(parentWindow)) {
			return switchWindow(parentWindow, driver);
		}
		return driver;
	}

	public static String getUrlPath(WebDriver driver) throws URISyntaxException {
		String url = driver.getCurrentUrl();
		URI uri = new URI(url);
		String path = uri.getPath();
		return path;
	}

	/**
	 * Opens new tab, switches to it, and returns parent window handle
	 * 
	 * @param url
	 * @param driver
	 * @return
	 */
	public static String openNewTabWithLink(String url, WebDriver driver) {
		String parentHandle = driver.getWindowHandle();
		String jsScript = "window.open('" + url + "','_blank');";
		new WebDriverWait(driver, 15).until(ExpectedConditions.javaScriptThrowsNoExceptions(jsScript));
		switchToChildWindow(parentHandle, driver);
		return parentHandle;
	}

	public static String openLink(String url, WebDriver driver) {
		String parentHandle = driver.getWindowHandle();
		String jsScript = "window.open('" + url + "','_self');";
		new WebDriverWait(driver, 15).until(ExpectedConditions.javaScriptThrowsNoExceptions(jsScript));
		return parentHandle;
	}

	public static void clickOnLink(WebElement element, WebDriver driver) {
		String jsScript = "arguments[0].click();";
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript(jsScript, element);
	}

	public static void jsScrollIntoView(WebElement element, WebDriver driver) {
		String jsScript = "arguments[0].scrollIntoView(true);";
		((JavascriptExecutor) driver).executeScript(jsScript, element);
	}

	public static String getTextOfElemenyLocatedBy(By by, WebDriver driver) {
		WebElement element = new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(by));
		String text = element.getText();
		return text;
	}

	public static WebElement getChildElementFromParentLocatedBy(WebElement parent, By relativeChildBy,
			WebDriver driver) {
		WebElement element = new WebDriverWait(driver, 15)
				.until(ExpectedConditions.visibilityOf(parent.findElement(relativeChildBy)));
		return element;
	}

	public static boolean isChildElementVisibleFromParentLocatedBy(WebElement parent, By relativeChildBy,
			WebDriver driver) {
		boolean retVal = false;
		try {
			getChildElementFromParentLocatedBy(parent, relativeChildBy, driver);
			retVal = true;
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	public static void clearInputField(WebElement inputField, WebDriver driver) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(inputField));
		String text = inputField.getAttribute("value");
		for (int i = 0; i < text.length(); i++) {
			inputField.sendKeys(Keys.BACK_SPACE);
		}
	}

}
