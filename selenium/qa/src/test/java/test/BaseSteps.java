package test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.Event;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import config.BrowserStackStatusEnum;
import config.BrowsersEnum;
import config.DriverFactory;
import pages.components.admin.events.EventSummaryComponent;
import test.facade.FacadeProvider;

public class BaseSteps {

	public WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	@Parameters(value = { "config", "environment" })
	public void setUp(@Optional String config, @Optional String environment) throws Exception {
		driver = DriverFactory.getDriverManager(config, environment).getDriver();
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		try {
			if (BrowsersEnum.REMOTE.equals(DriverFactory.getBrowser())) {

				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("methodName", result.getMethod().getMethodName());

				if (result.getStatus() == ITestResult.SUCCESS) {
					updateTestStatus(driver, BrowserStackStatusEnum.PASSED, parameters);
				} else if (result.getStatus() == ITestResult.FAILURE) {
					updateTestStatus(driver, BrowserStackStatusEnum.FAILED, parameters);
				}
			} else {
				analyzeLog();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			driver.quit();
		}
	}

	protected void deleteEvent(FacadeProvider fp, Event event) {
		try {
			fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
			EventSummaryComponent eventCard = fp.getAdminEventStepsFacade().findEventWithName(event);
			eventCard.clickOnDeleteEvent(event);
		} catch (Exception e) {
			//LOG
			e.printStackTrace();
		}
	}

	protected void cancelEvent(FacadeProvider fp, Event event) {
		try {
			fp.getAdminEventStepsFacade().givenUserIsOnAdminEventsPage();
			EventSummaryComponent eventCard = fp.getAdminEventStepsFacade().findEventWithName(event);
			eventCard.clickOnCancelEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateTestStatus(WebDriver driver, BrowserStackStatusEnum status, Map<String, String> parameters) {
		try {
			String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
			String username = DriverFactory.getUsername();
			String key = DriverFactory.getAccessKey();
			String url = "https://" + username + ":" + key + "@api.browserstack.com/automate/sessions/" + sessionId
					+ ".json";
			URI uri = new URI(url);
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("name", parameters.get("methodName")));
			nameValuePair.add(new BasicNameValuePair("status", status.getStatus()));
			restPutProvider(uri, nameValuePair);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void analyzeLog() {
		LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
		for (LogEntry entry : logEntries) {
			System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
		}
	}

	private void restPutProvider(URI uri, ArrayList<NameValuePair> content)
			throws ClientProtocolException, IOException {
		HttpPut putRequest = new HttpPut(uri);
		putRequest.setEntity(new UrlEncodedFormEntity(content));
		HttpClientBuilder.create().build().execute(putRequest);
	}

	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public void setWindowDimension(int width, int height) {
		Dimension d = new Dimension(width, height);
		driver.manage().window().setSize(d);
	}

}
