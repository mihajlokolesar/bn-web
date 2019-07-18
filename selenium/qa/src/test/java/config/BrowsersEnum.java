package config;

public enum BrowsersEnum {
	FIREFOX("ff"),
	CHROME("ch"),
	REMOTE("rm"),
	SAFARI("sf");
	
	private String browserCode;
	
	

	private BrowsersEnum(String browserCode) {
		this.browserCode = browserCode;
	}

	public String getBrowserCode() {
		return browserCode;
	}

	public void setBrowserCode(String browserCode) {
		this.browserCode = browserCode;
	}
	
	public static BrowsersEnum getEnumForCode(String code) {
		for(BrowsersEnum c : values()) {
			if(c.browserCode.equals(code)) {
				return c;
			}
		}
		return BrowsersEnum.SAFARI;
	}
	

}
