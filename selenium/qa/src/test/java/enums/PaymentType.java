package enums;

public enum PaymentType {

	CASH("cash"),
	CREDIT_CARD("creditCard");
	
	private String value;
	
	private PaymentType(String value) {
		this.value = value;
	}
	
}