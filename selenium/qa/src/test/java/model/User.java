package model;

import java.io.Serializable;

import utils.DataConstants;
import utils.ProjectUtils;

public class User implements Serializable {

	private static final long serialVersionUID = 8184904779942132639L;
	private String emailAddress;
	private String pass;
	private String passConfirm;
	private String firstName;
	private String lastName;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPassConfirm() {
		return passConfirm;
	}

	public void setPassConfirm(String passConfirm) {
		this.passConfirm = passConfirm;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] fields = { this.emailAddress, this.firstName, this.lastName, this.pass, this.passConfirm };
		ProjectUtils.appendFields(fields, sb);
		return sb.toString();
	}

	public static User generateSuperUser() {
		User user = new User();
		user.setEmailAddress("superuser@test.com");
		user.setPass("password");
		return user;
	}

	public static User generateUser() {
		User user = new User();
		user.setEmailAddress("bluetestneouser@mailinator.com");
		user.setPass("test1111");
		user.setFirstName("test");
		user.setLastName("testqa");
		return user;
	}

	public static User generateRandomUser() {
		User retVal = new User();
		String firstName = "seleniumtest";
		String lastName = "qaselenium";
		String emailAddress = firstName + ProjectUtils.generateRandomInt(1000000) + "@mailinator.com";
		String password = "seleniumpassword";
		String confirmPas = password;
		retVal.setEmailAddress(emailAddress);
		retVal.setFirstName(firstName);
		retVal.setLastName(lastName);
		retVal.setPass(password);
		retVal.setPassConfirm(confirmPas);
		return retVal;
	}

	public static User generateUser(String firstName, String lastName) {
		User retVal = new User();
		String email = firstName + "." + lastName + "@" + DataConstants.MAILINATOR_MAIL_DOMAIN;
		retVal.setEmailAddress(email);
		retVal.setFirstName(firstName);
		retVal.setLastName(lastName);
		retVal.setPass(DataConstants.USER_PASS);
		retVal.setPassConfirm(DataConstants.USER_PASS);
		return retVal;
	}
}
