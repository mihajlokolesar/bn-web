package model;

import java.io.Serializable;

public class Purchase implements Serializable {

	private static final long serialVersionUID = 4491179406895426817L;
	private Event event;
	private CreditCard creditCard;
	private int numberOfTickets;
	private int removeNumberOfTickets;
	private String phoneNumber;

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
	
	
}
