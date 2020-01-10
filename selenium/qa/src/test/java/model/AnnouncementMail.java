package model;

import com.fasterxml.jackson.core.type.TypeReference;

import utils.DataReader;

public class AnnouncementMail {

	private String email;
	private String subject;
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static TypeReference<AnnouncementMail> getTypeReference() {
		return new TypeReference<AnnouncementMail>() {
		};
	}

	public static AnnouncementMail generateAnnouncementFromJson(String key) {
		return (AnnouncementMail) DataReader.getInstance().getObject(key, getTypeReference());
	}

}
