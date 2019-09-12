package model;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdditionalOptionsTicketType implements Serializable {
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum SaleStart {
		IMMEDIATELY("Immediately","immediately"), AT_SPECIFIC_TIME("At a specific time","custom"), WHEN_SALES_END_FOR("When sales end for...","parent");

		private String label;
		private String value;

		private SaleStart(String label, String value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public String getValue() {
			return value;
		}
		
		@JsonCreator
		static SaleStart findValue(@JsonProperty("label") String label, @JsonProperty("value") String value) {
			return Arrays.stream(SaleStart.values()).filter(se -> se.label.equals(label) && se.value.equals(value)).findFirst().get();
		}

	}
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum SaleEnd {
		DOOR_TIME("Door Time","door"), EVENT_START("Event Start","start"), EVENT_END("Event End","close"),
		AT_SPECIFIC_TIME("At a specific time","custom");
		
		private String label;
		private String value;

		private SaleEnd(String label, String value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}
		
		public String getValue() {
			return value;
		}
		
		@JsonCreator
		static SaleEnd findValue(@JsonProperty("label") String label, @JsonProperty("value") String value) {
			return Arrays.stream(SaleEnd.values()).filter(se -> se.label.equals(label) && se.value.equals(value)).findFirst().get();
		}

	}
	
	private static final long serialVersionUID = -3276023451403139476L;
	@JsonProperty("sale_start")
	private SaleStart saleStart;
	@JsonProperty("sale_end")
	private SaleEnd saleEnd;

	@JsonProperty("start_sale_date")
	private String startSaleDate;
	@JsonProperty("start_sale_time")
	private String startSaleTime;
	@JsonProperty("start_sale_ticket_type")
	private String startSaleTicketType;

	@JsonProperty("end_sale_date")
	private String endSaleDate;
	@JsonProperty("end_sale_time")
	private String endSaleTime;

	public SaleStart getSaleStart() {
		return saleStart;
	}

	public void setSaleStart(SaleStart saleStart) {
		this.saleStart = saleStart;
	}

	public SaleEnd getSaleEnd() {
		return saleEnd;
	}

	public void setSaleEnd(SaleEnd saleEnd) {
		this.saleEnd = saleEnd;
	}

	public String getStartSaleDate() {
		return startSaleDate;
	}

	public void setStartSaleDate(String startSaleDate) {
		this.startSaleDate = startSaleDate;
	}

	public String getStartSaleTime() {
		return startSaleTime;
	}

	public void setStartSaleTime(String startSaleTime) {
		this.startSaleTime = startSaleTime;
	}

	public String getStartSaleTicketType() {
		return startSaleTicketType;
	}

	public void setStartSaleTicketType(String startSaleTicketType) {
		this.startSaleTicketType = startSaleTicketType;
	}

	public String getEndSaleDate() {
		return endSaleDate;
	}

	public void setEndSaleDate(String endSaleDate) {
		this.endSaleDate = endSaleDate;
	}

	public String getEndSaleTime() {
		return endSaleTime;
	}

	public void setEndSaleTime(String endSaleTime) {
		this.endSaleTime = endSaleTime;
	}

}
