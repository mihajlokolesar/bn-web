package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class PurchaseMailFrame extends BaseComponent {
	
	@FindBy(xpath = "//table//tr[th[contains(text(),'Units')]]/following-sibling::tr/td[1]")
	private WebElement quantity;
	
	@FindBy(xpath = "//table//tr[th[contains(text(),'Units')]]/following-sibling::tr/td[2]")
	private WebElement eventName;
	
	@FindBy(xpath = "//table//td[contains(text(),'Order Total')]")
	private WebElement orderTotal;
	
	public PurchaseMailFrame(WebDriver driver) {
		super(driver);
	}
	
    public String getQuantity() {
    	explicitWaitForVisiblity(quantity);
    	return quantity.getText();
    }
    
    public String getEventName() {
    	explicitWaitForVisiblity(eventName);
    	return eventName.getText();
    }
    
    public Double getOrderTotal() {
    	explicitWaitForVisiblity(orderTotal);
    	String text = orderTotal.getText().trim();
    	String[] tokens = text.split("\\$");
    	Double orderTotal = Double.parseDouble(tokens[1]);
    	return orderTotal;
    }
}
