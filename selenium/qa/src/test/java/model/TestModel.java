package model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestModel implements Serializable{
	
	@JsonProperty("test")
	private boolean test;

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}
}
