package data.holders.ticket.order;

import java.math.BigDecimal;

import data.holders.DataHolder;
import model.Event;
import model.User;
import model.Venue;

public class OrderDetailsData implements DataHolder {
	
	private BigDecimal orderTotal;
	private BigDecimal feesSubtotal;
	private BigDecimal subtotal;
	private String ticketType;
	private BigDecimal ticketPrice;
	private BigDecimal qty;
	private BigDecimal ticketTotal;
	private String orderNumber;
	private Event event;
	private Venue venue;
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public BigDecimal getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
	}
	public BigDecimal getFeesSubtotal() {
		return feesSubtotal;
	}
	public void setFeesSubtotal(BigDecimal feesSubtotal) {
		this.feesSubtotal = feesSubtotal;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getTicketTotal() {
		return ticketTotal;
	}
	public void setTicketTotal(BigDecimal ticketTotal) {
		this.ticketTotal = ticketTotal;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Venue getVenue() {
		return venue;
	}
	public void setVenue(Venue venue) {
		this.venue = venue;
	}

}
