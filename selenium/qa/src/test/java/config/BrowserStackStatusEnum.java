package config;

public enum BrowserStackStatusEnum {

	PASSED("completed"), FAILED("failed");

	private String status;

	private BrowserStackStatusEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
