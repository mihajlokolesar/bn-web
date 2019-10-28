package model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import model.organization.OtherFees;
import utils.DataConstants;
import utils.DataReader;
import utils.ProjectUtils;

public class Organization implements Serializable {
	
	private static final long serialVersionUID = -2120443225758565920L;
	@JsonProperty("name")
	private String name;
	@JsonProperty("phone_number")
	private String phoneNumber;
	@JsonProperty("time_zone")
	private String timeZone;
	@JsonProperty("location")
	private String location;
	@JsonProperty("other_fees")
	private OtherFees otherFees;
		
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
	
	public OtherFees getOtherFees() {
		return otherFees;
	}
	public void setOtherFees(OtherFees otherFees) {
		this.otherFees = otherFees;
	}
	public void randomizeName() {
		this.name = this.name + ProjectUtils.generateRandomInt(DataConstants.RANDOM_NUMBER_SIZE_10M);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(this.getName()).toString();
	}
	public static Organization generateOrganization() {
		Organization organization = new Organization();
		organization.setName("Auto test " + ProjectUtils.generateRandomInt(1000000));
		organization.setPhoneNumber("1111111111");
		organization.setTimeZone("Africa/Johannesburg");
		organization.setLocation("Johannesburg, South Africa");
		return organization;
	}
	
	public static Organization generateOrganizationFromJson(String key, boolean randomizeName) {
		Organization organization = (Organization) DataReader.getInstance().getObject(key, new TypeReference<Organization>() {
		});
		if (randomizeName) {
			organization.randomizeName();
		}
		return organization;
	}
	
}
