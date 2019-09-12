package model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketType implements Serializable{
	
	private static final long serialVersionUID = 4807652309876731369L;
	@JsonProperty("ticket_type_name")
	private String ticketTypeName;
	@JsonProperty("capacity")
	private String capacity;
	@JsonProperty("price")
	private String price;
	@JsonProperty("additional_options")
	private AdditionalOptionsTicketType additionalOptions;
	
		
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
	public AdditionalOptionsTicketType getAdditionalOptions() {
		return additionalOptions;
	}
	public void setAdditionalOptions(AdditionalOptionsTicketType additionalOptions) {
		this.additionalOptions = additionalOptions;
	}
	
	
	
	
}
