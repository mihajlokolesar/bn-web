package model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	
	public static CreditCard generateCreditCard() {
		CreditCard card = new CreditCard();
		card.setCardNumber("4242424242424242");
		card.setExpirationDate("0442");
		card.setCvc("424");
		card.setZipCode("24242");
		return card;
	}
	
}
