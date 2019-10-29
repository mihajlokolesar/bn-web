package config;

public enum BrowsersEnum {
	FIREFOX("ff","firefox"),
	CHROME("ch","chrome"),
	REMOTE("rm","remote"),
	SAFARI("sf","safari"),
	HUB("hub", "hub");
	
	private String browserCode;
	private String name;
	

	private BrowsersEnum(String browserCode, String name) {
		this.browserCode = browserCode;
		this.name = name;
	}

	public String getBrowserCode() {
		return browserCode;
	}
	
	public String getName() {
		return name;
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
