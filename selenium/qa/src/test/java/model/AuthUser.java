package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import utils.DataReader;

public class AuthUser {
	
	@JsonProperty("username")
	private String username;
	@JsonProperty("password")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserInfo() {
		return this.username+":"+this.password;
	}
	
	public static AuthUser generateFromJson(String key) {
		return (AuthUser) DataReader.getInstance().getObject(key, getTypeReference());
	}
	
	public static TypeReference<AuthUser> getTypeReference(){
		return new TypeReference<AuthUser>() {};
	}
}