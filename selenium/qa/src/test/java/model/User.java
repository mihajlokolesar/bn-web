package model;

public class User {
	
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
		return user;
	}

}
