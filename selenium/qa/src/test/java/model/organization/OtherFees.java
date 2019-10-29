package model.organization;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import enums.SettlementType;

public class OtherFees {
	
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
}