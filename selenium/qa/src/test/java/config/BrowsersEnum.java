package config;

public enum BrowsersEnum {
	FIREFOX("ff","firefox"),
	CHROME("ch","chrome"),
	REMOTE("rm","remote"),
	SAFARI("sf","safari");
	
	private String browserCode;
	private String name;
	

	private BrowsersEnum(String browserCode, String b) {
		this.browserCode = browserCode;
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
