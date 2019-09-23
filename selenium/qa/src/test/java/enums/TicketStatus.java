package enums;

public enum TicketStatus {

	PURCHASED("Purchased"), REFUNDED("Refunded");

	private String value;

	private TicketStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static TicketStatus getTicketStatus(String value) {
		TicketStatus[] enums = values();
		for (TicketStatus ts : enums) {
			if (ts.getValue().equalsIgnoreCase(value)) {
				return ts;
			}
		}
		return null;
	}

}
