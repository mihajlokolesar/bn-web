package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utils.ProjectUtils;

public class Event implements Serializable {

	private static final long serialVersionUID = 6081396679346519203L;
	private Organization organization;
	private String artistName;
	private String eventName;
	private String venueName;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String doorTime;
	private List<TicketType> ticketTypes = new ArrayList<>();

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDoorTime() {
		return doorTime;
	}

	public void setDoorTime(String doorTime) {
		this.doorTime = doorTime;
	}

	public List<TicketType> getTicketTypes() {
		return ticketTypes;
	}

	public void setTicketTypes(List<TicketType> ticketTypes) {
		this.ticketTypes = ticketTypes;
	}

	public void addTicketType(TicketType ticketType) {
		this.ticketTypes.add(ticketType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] fields = { this.eventName, this.artistName, this.venueName, this.startDate, this.endDate,
				this.startTime, this.endDate,
				this.getOrganization() != null ? this.getOrganization().getName() : null };
		ProjectUtils.appendFields(fields, sb);
		return sb.toString();

	}

	public static Event generateEvent() {
		Event event = new Event();
		Organization organization = Organization.generateOrganization();
		organization.setName("Auto Test12");
		event.setOrganization(organization);
		event.setArtistName("TheTestArtistAuto");
		event.setEventName("TestNameEvent" + ProjectUtils.generateRandomInt(10000000));
		event.setVenueName("MSG");
		String[] dateSpan = ProjectUtils.getDatesWithSpecifiedRangeInDays(2);
		String startDate = dateSpan[0];
		String endDate = dateSpan[1];
		event.setStartDate(startDate);
		event.setEndDate(endDate);
		event.setStartTime("08:30 PM");
		event.setEndTime("10:00 PM");
		event.setDoorTime("1");
		TicketType ticketType1 = new TicketType("GA", "100", "1");
		TicketType ticketType2 = new TicketType("VIP", "70", "2");
		event.addTicketType(ticketType1);
		event.addTicketType(ticketType2);

		return event;
	}
	
	public static Event generatedEvent(int daysOffsetStart, int dateRange, String eventName, boolean addRandomToName) {
		Event event = new Event();
		Organization organization = Organization.generateOrganization();
		organization.setName("Auto Test12");
		event.setOrganization(organization);
		event.setArtistName("TheTestArtistAuto");
		event.setEventName(eventName + (addRandomToName? ProjectUtils.generateRandomInt(10000000) : ""));
		event.setVenueName("MSG");
		String[] dateSpan = ProjectUtils.getDatesWithSpecifiedRangeInDaysWithStartOffset(daysOffsetStart, dateRange);
		String startDate = dateSpan[0];
		String endDate = dateSpan[1];
		event.setStartDate(startDate);
		event.setEndDate(endDate);
		event.setStartTime("08:30 PM");
		event.setEndTime("10:00 PM");
		event.setDoorTime("1");
		TicketType ticketType1 = new TicketType("GA", "100", "1");
		TicketType ticketType2 = new TicketType("VIP", "70", "2");
		event.addTicketType(ticketType1);
		event.addTicketType(ticketType2);

		return event;
	}

}
