package pages.components.admin.orders.manage.tickets;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pages.BaseComponent;

public class OrderDetails extends BaseComponent {

	@FindBy(xpath = "//main//div[div[div[div[div[p[text()='Ticket #']]]]]]")
	private WebElement container;

	private final String IS_EXPANDED_CONDITION = "min-height: 0px; height: auto; transition-duration: 300ms;";

	private final String IS_CLOSED_CONDITION = "min-height: 0px; height: 0px; transition-duration: 300ms;";

	@FindAll(value = { @FindBy(xpath = "//main//div[p[text()='Ticket #']]/following-sibling::div") })
	private List<WebElement> rows;

	public OrderDetails(WebDriver driver) {
		super(driver);
	}

	public boolean isExpanded() {
		return isExplicitConditionTrue(5,
				ExpectedConditions.attributeContains(container, "style", IS_EXPANDED_CONDITION));
	}

	public boolean isClosed() {
		return isExplicitConditionTrue(5,
				ExpectedConditions.attributeContains(container, "style", IS_CLOSED_CONDITION));
	}

	public TicketRow findTicketRow(Predicate<TicketRow> predicate) {
		Optional<TicketRow> row = rows.stream().map(e -> new TicketRow(driver, e)).filter(predicate).findFirst();
		return row.isPresent() ? row.get() : null;
	}
}