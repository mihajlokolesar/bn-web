package pages.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pages.BaseComponent;

public class AddTicketTypeComponent extends BaseComponent{
	
	
	@FindBy(xpath = "//main//button//img[@alt='Edit']")
	private WebElement editSaveButton;
	
	@FindBy(xpath = "//main//button//img[@alt='Delete']")
	private WebElement cancelDeleteButton;
	
	@FindBy(id = "name")
	private WebElement ticketNameField;
	
	//type=number
	@FindBy(id = "capacity")
	private WebElement capacityField;
	
	//type=number
	@FindBy(id = "value")
	private WebElement priceValueField;

	public AddTicketTypeComponent(WebDriver driver) {
		super(driver);
	}
	
	public void addNewTicketType(String name, String capacity, String price) {
		waitVisibilityAndSendKeys(ticketNameField, name);
		waitVisibilityAndSendKeys(capacityField, capacity);
		waitVisibilityAndSendKeys(priceValueField, price);
		waitVisibilityAndClick(editSaveButton);
		
	}

}
