package utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumUtils {
	
	public static WebDriver switchToFrame(WebDriver driver, WebElement iframe ) {
		driver.switchTo().frame(iframe);
		return driver;
	}
	
	public static WebDriver switchToParentWindow(String parentHandle, WebDriver driver) {
		String currentHandle = driver.getWindowHandle();
		if(!currentHandle.equalsIgnoreCase(parentHandle)) {
			driver.switchTo().window(parentHandle);
		}
		return driver;
	}
	
	public static WebDriver switchWindow(String currentHandle, WebDriver driver) {
		List<String> handles = new ArrayList<String>(driver.getWindowHandles());
		for(String h : handles) {
			if(!currentHandle.equalsIgnoreCase(h)) {
				driver.switchTo().window(h);
			}
		}
		return driver;
	}
	
	public static WebDriver switchToChildWindow(String parentWindow, WebDriver driver) {
		String current = driver.getWindowHandle();
		if(current.equalsIgnoreCase(parentWindow)) {
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

}
