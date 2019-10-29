package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JsUtils {
	
	private WebDriver driver;

	public JsUtils(WebDriver driver) {
		super();
		this.driver = driver;
	}
	
	public String openLink(String url) {
		String parentHandle = driver.getWindowHandle();
		String jsScript = "window.open('" + url + "','_self');";
		new WebDriverWait(driver, 15).until(ExpectedConditions.javaScriptThrowsNoExceptions(jsScript));
		return parentHandle;
	}

	public void clickOnElement(WebElement element) {
		String jsScript = "arguments[0].click();";
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript(jsScript, element);
	}

	public void jsScrollIntoView(WebElement element) {
		String jsScript = "arguments[0].scrollIntoView(true);";
		((JavascriptExecutor) driver).executeScript(jsScript, element);
	}
}