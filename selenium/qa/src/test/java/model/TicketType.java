package model;

public class TicketType {
	
	private String ticketTypeName;
	private String capacity;
	private String price;
		
	public TicketType(String ticketTypeName, String capacity, String price) {
		super();
		this.ticketTypeName = ticketTypeName;
		this.capacity = capacity;
		this.price = price;
	}
	public TicketType() {
		super();
	}
	public String getTicketTypeName() {
		return ticketTypeName;
	}
	public void setTicketTypeName(String ticketTypeName) {
		this.ticketTypeName = ticketTypeName;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
