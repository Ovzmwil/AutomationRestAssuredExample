package com.leandro.restassured.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestHelper {
	
	ObjectMapper mapper = new ObjectMapper();
	
	public String createRequest(Object obj) throws JsonProcessingException {
		String jsonInString = mapper.writeValueAsString(obj);
		return jsonInString;
	}

}
