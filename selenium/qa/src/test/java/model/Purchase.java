package model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import utils.DataReader;

public class Purchase implements Serializable {

	private static final long serialVersionUID = 4491179406895426817L;
	private Event event;
	@JsonProperty("credit_card")
	private CreditCard creditCard;
	@JsonProperty("add_ticket_number")
	private int numberOfTickets;
	@JsonProperty("remove_ticket_number")
	private int removeNumberOfTickets;
	@JsonProperty("order_note")
	private String orderNote;
	@JsonProperty("additional_tendered_amount")
	private int additionalTenderedAmount;
	@JsonProperty("phone_number")
	private String phoneNumber;

	public Purchase() {
		super();
	}

	public Purchase(Event event, CreditCard creditCard, int numberOfTickets) {
		super();
		this.event = event;
		this.creditCard = creditCard;
		this.numberOfTickets = numberOfTickets;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getRemoveNumberOfTickets() {
		return removeNumberOfTickets;
	}

	public void setRemoveNumberOfTickets(int removeNumberOfTickets) {
		this.removeNumberOfTickets = removeNumberOfTickets;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}
	
	public int getAdditionalTenderedAmount() {
		return additionalTenderedAmount;
	}

	public void setAdditionalTenderedAmount(int additionalTenderedAmount) {
		this.additionalTenderedAmount = additionalTenderedAmount;
	}

	public static Object[] generatePurchasesFromJson(String key) {
		return DataReader.getInstance().getObjects(key, new TypeReference<List<Purchase>>() {
		});
	}

	public static Purchase generatePurchaseFromJson(String key) {
		return (Purchase) DataReader.getInstance().getObject(key, new TypeReference<Purchase>() {
		});
	}

	public static void addEventsToPurchases(Object[] events, Object[] purchases) {
		if (events.length != purchases.length) {
			return;
		}
		for (int i = 0; i < purchases.length; i++) {
			((Purchase)purchases[i]).setEvent((Event)events[i]);
		}
	}

}
