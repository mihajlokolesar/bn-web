package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.jws.soap.SOAPBinding;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.User;

public class DataReader {

	private String dataProviderFile = "test_data.json";

	private JSONObject jsonObject;

	private static class Loader {
		public static DataReader INSTANCE = new DataReader();
	}

	private DataReader() {
		try {
			JSONParser parser = new JSONParser();
			String fileName = "";
			String baseUrl = System.getProperty("baseurl");
			if (baseUrl.contains("develop")) {
				fileName = "develop_" + dataProviderFile;
			} else if (baseUrl.contains("beta")) {
				fileName = "beta_" + dataProviderFile;
			} else if (baseUrl.contains("local")) {
				fileName = "local_" + dataProviderFile;
			} else {
				fileName = "develop_" + dataProviderFile;
			}
			jsonObject = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DataReader getInstance() {
		return Loader.INSTANCE;
	}

	public Object[][] getSingleEntityData(String key, TypeReference typeReference, int destinationColumn)
			throws Exception {
		Object[] obj = getObjects(key, typeReference);
		Object[][] data = new Object[obj.length][1];
		ProjectUtils.composeData(data, obj, destinationColumn);
		return data;
	}

	public Object[] getObjects(String key, TypeReference typeReference) {
		JSONArray array = getJsonArray(key);
		String sArray = array.toJSONString();
		ObjectMapper mapper = getMapper();
		List retVal = null;
		try {
			retVal = mapper.readValue(sArray, typeReference);
		} catch (Exception e) {
			System.out.println();
		}
		return retVal != null ? retVal.toArray() : null;
	}

	public Object getObject(String key, TypeReference typeReference) {
		JSONObject object = getJsonObject(key);
		String sObject = object.toJSONString();
		Object retVal = null;
		try {
			retVal = getMapper().readValue(sObject, typeReference);
		} catch (Exception e) {
			System.out.println();
		}

		return retVal;

	}

	private ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public JSONObject getJsonObject(String key) {
		return (JSONObject) jsonObject.get(key);
	}

	public JSONArray getJsonArray(String key) {
		return (JSONArray) jsonObject.get(key);
	}

}
