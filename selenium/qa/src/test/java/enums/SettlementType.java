package enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SettlementType {
	
	POST_EVENT("Post event","PostEvent"),
	ROLLING("Rolling","Rolling");
	
	private String label;
	private String value;
	
	private SettlementType(String label, String value) {
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
	static SettlementType findValue(@JsonProperty("label") String label, @JsonProperty("value") String value) {
		return Arrays.stream(SettlementType.values()).filter(st -> st.label.equals(label) && st.value.equals(value)).findFirst().get();
	}
}