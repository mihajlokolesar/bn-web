package model;

import utils.ProjectUtils;

public class Organization {
	
	private String name;
	private String phoneNumber;
	private String timeZone;
	private String location;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
		
	public static Organization generateOrganization() {
		Organization organization = new Organization();
		organization.setName("Auto test " + ProjectUtils.generateRandomInt(1000000));
		organization.setPhoneNumber("1111111111");
		organization.setTimeZone("Africa/Johannesburg");
		organization.setLocation("Johannesburg, South Africa");
		return organization;
	}
	
}
