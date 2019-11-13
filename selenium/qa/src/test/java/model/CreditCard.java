package model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import utils.DataReader;

public class CreditCard implements Serializable{
	
	
	private static final long serialVersionUID = 4295582882806128546L;
	@JsonProperty("card_number")
	private String cardNumber;
	@JsonProperty("expiration_date")
	private String expirationDate;
	@JsonProperty("cvc")
	private String cvc;
	@JsonProperty("zip_code")
	private String zipCode;
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getCvc() {
		return cvc;
	}
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public static TypeReference<List<CreditCard>> getListTypeReference() {
		return new TypeReference<List<CreditCard>>() {
		};
	}
	
	public static TypeReference<CreditCard> getTypeReference() {
		return new TypeReference<CreditCard>() {
		};
	}
	
	public static CreditCard generateCreditCardFromJson(String key) {
		return (CreditCard) DataReader.getInstance().getObject(key, getTypeReference());
	}
	
}
