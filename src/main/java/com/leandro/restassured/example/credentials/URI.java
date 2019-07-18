package com.leandro.restassured.example.credentials;

public class URI {
	
	private final static String riotURI = "https://br1.api.riotgames.com";
	private final static String testURI = "https://my-json-server.typicode.com/Ovzmwil/MyJsonServer";
	private final static String hdiURI = "";

	public static String getHdiURI() {
		return hdiURI;
	}

	public static String getRiotURI() {
		return riotURI;
	}
	
	public static String getTestURI() {
		return testURI;
	}

}
