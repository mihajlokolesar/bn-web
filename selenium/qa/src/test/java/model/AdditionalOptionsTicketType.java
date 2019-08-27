package model;

import java.io.Serializable;

public class AdditionalOptionsTicketType implements Serializable {

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

	}

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

	}
	
	private static final long serialVersionUID = -3276023451403139476L;
	private SaleStart saleStart;
	private SaleEnd saleEnd;

	private String startSaleDate;
	private String startSaleTime;
	private String startSaleTicketType;

	private String endSaleDate;
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
