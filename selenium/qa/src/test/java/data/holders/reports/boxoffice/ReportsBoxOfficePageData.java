package data.holders.reports.boxoffice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.holders.DataHolder;

public class ReportsBoxOfficePageData implements Serializable, DataHolder {
	
	private static final long serialVersionUID = -3026470462402533916L;
	private List<OperatorTableData> tables;
	private Map<String,OperatorTableRowData> rows;
	private Map<String,EventContainer> eventsData;
	private BigDecimal cashTotal;
	private BigDecimal creditCardTotal;
	private LocalDate fromDate;
	private LocalDate toDate;
	
	public ReportsBoxOfficePageData() {
		this.eventsData = new HashMap<>();
		this.rows = new HashMap<>();
		this.tables = new ArrayList<>();
		this.cashTotal = new BigDecimal(0);
		this.creditCardTotal = new BigDecimal(0);
	}
	
	public List<OperatorTableData> getTables() {
		return tables;
	}
	public void setTables(List<OperatorTableData> tables) {
		this.tables = tables;
	}
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	/**
	 * This field is intended to hold rows each unique event, so if some events are present in multiple 
	 * operator tables they will be all put under the same key, which is Event Name 
	 * 
	 */
	public Map<String, OperatorTableRowData> getRows() {
		return rows;
	}
	public void setRows(Map<String, OperatorTableRowData> rows) {
		this.rows = rows;
	}
		
	public BigDecimal getCashTotal() {
		return cashTotal;
	}

	public void setCashTotal(BigDecimal cashTotal) {
		this.cashTotal = cashTotal;
	}

	public BigDecimal getCreditCardTotal() {
		return creditCardTotal;
	}

	public void setCreditCardTotal(BigDecimal creditCardTotal) {
		this.creditCardTotal = creditCardTotal;
	}

	public void add(OperatorTableData table) {
		tables.add(table);
		updateCashAndCreditTotal(table);
		for(OperatorTableRowData row : table.getRows()) {
			if(rows.get(row.getEventName()) == null) {
				rows.put(row.getEventName(), row);
			}
			updateEventsDataMap(row);
		}
	}
	
	private void updateEventsDataMap(OperatorTableRowData row) {
		EventContainer dataContainer = eventsData.get(row.getEventName());
		if (dataContainer == null) {
			dataContainer = new EventContainer();
			dataContainer.setEventName(row.getEventName());
			dataContainer.addToSoldQty(row.getBoxOfficeSold());
			dataContainer.addToTotal(row.getTotalValue());
			eventsData.put(dataContainer.getEventName(), dataContainer);
		} else {
			dataContainer.addToSoldQty(row.getBoxOfficeSold());
			dataContainer.addToTotal(row.getTotalValue());
		}
	}
	
	private void updateCashAndCreditTotal(OperatorTableData table) {
		this.cashTotal = this.cashTotal.add(table.getCashRow().getTotalValue());
		this.creditCardTotal = this.creditCardTotal.add(table.getCreditCardRow().getTotalValue());
	}
	
	public boolean isZoneEqualTo(ZoneId zoneId) {
		for(OperatorTableData tableData : tables) {
			if (!tableData.isDateZoneEqual(zoneId)) {
				return false;
			}
		}
		return true;
	}
	
	public BigDecimal getEventTotal(String eventName) {
		EventContainer eventCont = eventsData.get(eventName);
		if (eventCont != null) {
			return eventCont.getTotal();
		}
		return null;
	}
	
	public BigDecimal getEventSoldToQty(String eventName) {
		EventContainer eventCont = eventsData.get(eventName);
		if (eventCont != null) {
			return eventCont.getSoldQty();
		}
		return null;
	}
	
	
	private class EventContainer {
		
		private String eventName;
		private BigDecimal total;
		private BigDecimal soldQty;
		
		public EventContainer() {
		}

		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public BigDecimal getTotal() {
			return total;
		}

		public void setTotal(BigDecimal total) {
			this.total = total;
		}

		public BigDecimal getSoldQty() {
			return soldQty;
		}
		
		public void setSoldQty(BigDecimal soldQty) {
			this.soldQty = soldQty;
		}

		public void addToTotal(BigDecimal otherTotal) {
			if (this.total == null) {
				this.total = new BigDecimal(0);
			}
			this.total = this.total.add(otherTotal);
		}
		
		public void addToSoldQty(int otherSoldQty) {
			if (this.soldQty == null) {
				this.soldQty = new BigDecimal(0);
			}
			this.soldQty = this.soldQty.add(new BigDecimal(otherSoldQty));
		}
		
	}
	
}