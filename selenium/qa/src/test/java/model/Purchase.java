package model;

public class Purchase {

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
