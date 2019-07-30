package test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import config.BrowserStackStatusEnum;
import config.BrowsersEnum;
import config.DriverFactory;

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
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			driver.quit();
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

	private void restPutProvider(URI uri, ArrayList<NameValuePair> content)
			throws ClientProtocolException, IOException {
		HttpPut putRequest = new HttpPut(uri);
		putRequest.setEntity(new UrlEncodedFormEntity(content));
		HttpClientBuilder.create().build().execute(putRequest);
	}

	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

}
