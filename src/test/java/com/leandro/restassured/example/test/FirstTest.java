package com.leandro.restassured.example.test;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.leandro.restassured.example.report.GenerateReport;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class FirstTest {

    Response response;
    
    @BeforeMethod
    public void set() {
    	RestAssured.baseURI = "https://br1.api.riotgames.com";
    }
    
    @Test
    public void test1() {
    	response = given().given().param("api_key", "RGAPI-e6cbd037-b3a8-47f2-b619-2e4989c2c0e0").when().get("/lol/platform/v3/champion-rotations");
    	
    	System.out.println(response.asString());
    }
	
}
