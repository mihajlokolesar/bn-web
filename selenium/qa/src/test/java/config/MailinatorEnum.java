package config;

public enum MailinatorEnum {
	
	TICKET_TRANSFER_CANCEL("ticket_transfer_cancel"),
	BO_SELL_WITH_CASH("bo_sell_with_cash"),
	RESET_PASSWORD("reset_password");
	
	private String label;
	
	private MailinatorEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
