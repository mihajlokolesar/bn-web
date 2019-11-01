package model.organization;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeesSchedule implements Serializable {

	private static final long serialVersionUID = -5689414932319621865L;

	@JsonProperty("fee_schedule_name")
	private String feeSceheduleName;
	@JsonProperty("minimum_price")
	private BigDecimal minimumPrice;
	@JsonProperty("client_fee")
	private BigDecimal clientFee;
	@JsonProperty("big_neon_fee")
	private BigDecimal bigNeonFee;
	
	public String getFeeSceheduleName() {
		return feeSceheduleName;
	}
	public void setFeeSceheduleName(String feeSceheduleName) {
		this.feeSceheduleName = feeSceheduleName;
	}
	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}
	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}
	public BigDecimal getClientFee() {
		return clientFee;
	}
	public void setClientFee(BigDecimal clientFee) {
		this.clientFee = clientFee;
	}
	public BigDecimal getBigNeonFee() {
		return bigNeonFee;
	}
	public void setBigNeonFee(BigDecimal bigNeonFee) {
		this.bigNeonFee = bigNeonFee;
	}
	
	public BigDecimal getTotalForNumberOfTickets(int numberOfTickets) {
		BigDecimal perTicketFee = getBigNeonFee().add(getClientFee());
		return perTicketFee.multiply(new BigDecimal(numberOfTickets));
	}
		
}