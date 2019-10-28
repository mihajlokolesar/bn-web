package model.organization;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import enums.SettlementType;

public class OtherFees implements Serializable {

	private static final long serialVersionUID = -8204980862351531684L;

	@JsonProperty("settlement_type")
	private SettlementType settlementType;
	
	@JsonProperty("per_order_client_fee")
	private BigDecimal perOrderClientFee;
	
	@JsonProperty("per_order_big_neon_fee")
	private BigDecimal perOrderBigNeonFee;
	
	@JsonProperty("credit_card_fee")
	private BigDecimal creditCardFee;

	public SettlementType getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(SettlementType settlementType) {
		this.settlementType = settlementType;
	}

	public BigDecimal getPerOrderClientFee() {
		return perOrderClientFee;
	}

	public void setPerOrderClientFee(BigDecimal perOrderClientFee) {
		this.perOrderClientFee = perOrderClientFee;
	}

	public BigDecimal getPerOrderBigNeonFee() {
		return perOrderBigNeonFee;
	}

	public void setPerOrderBigNeonFee(BigDecimal perOrderBigNeonFee) {
		this.perOrderBigNeonFee = perOrderBigNeonFee;
	}

	public BigDecimal getCreditCardFee() {
		return creditCardFee;
	}

	public void setCreditCardFee(BigDecimal creditCardFee) {
		this.creditCardFee = creditCardFee;
	}
	
	public BigDecimal getFixedFeesSum() {
		BigDecimal retVal = new BigDecimal(0);
		retVal = getPerOrderClientFee() != null ? retVal.add(getPerOrderClientFee()) : new BigDecimal(0);
		retVal = getPerOrderBigNeonFee() != null ? retVal.add(getPerOrderBigNeonFee()) : new BigDecimal(0);
		return retVal;
	}
	
	public BigDecimal getTotalWithFees(BigDecimal orderTotal) {
		BigDecimal fixedFees = getFixedFeesSum();
		orderTotal = orderTotal.add(fixedFees);
		orderTotal = applyCreditCardFee(orderTotal);
		return orderTotal;
	}
	
	public BigDecimal applyCreditCardFee(BigDecimal orderTotal) {
		if (getCreditCardFee() != null) {
			BigDecimal decimalCreditCardFeePercent = getCreditCardFee().divide(new BigDecimal(100));
			BigDecimal orderTotalPercent = orderTotal.multiply(decimalCreditCardFeePercent);
			BigDecimal retVal =  orderTotal.add(orderTotalPercent);
			return retVal;
		} else {
			return orderTotal;
		}
	}
}