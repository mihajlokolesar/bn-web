package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import utils.DataReader;

public class AnnouncementMail {

	@JsonProperty("address")
	private String address;
	@JsonProperty("subject")
	private String subject;
	@JsonProperty("body")
	private String body;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static TypeReference<AnnouncementMail> getTypeReference() {
		return new TypeReference<AnnouncementMail>() {
		};
	}

	public static AnnouncementMail generateAnnouncementFromJson(String key) {
		return (AnnouncementMail) DataReader.getInstance().getObject(key, getTypeReference());
	}

}
