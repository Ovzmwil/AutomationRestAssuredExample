package com.leandro.restassured.example.test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.leandro.restassured.example.credentials.Keys;
import com.leandro.restassured.example.credentials.URI;
import com.leandro.restassured.example.report.GenerateReport;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class FirstTest extends GenerateReport {

    Response response;
    
    @BeforeMethod
    public void set() {
    	RestAssured.baseURI = URI.getRiotURI();
    }
    
    @Test
    public void freeWeekTestParamKey() {
    	response = given().param("api_key", Keys.getApikey()).when().get("/lol/platform/v3/champion-rotations");
    	
    	assertThat(response.getStatusCode(), equalTo(200));
    	assertThat(response.jsonPath().getInt("freeChampionIds.size()"), equalTo(14));
    	assertThat(response.jsonPath().getInt("freeChampionIdsForNewPlayers.size()"), equalTo(10));
    	assertThat(response.jsonPath().getInt("maxNewPlayerLevel"), equalTo(10));
    }
    
    @Test
    public void freeWeekTestHeaderKey() {
    	response = given().header("X-Riot-Token", Keys.getApikey()).when().get("/lol/platform/v3/champion-rotations");
    	
    	assertThat(response.getStatusCode(), equalTo(200));
    	assertThat(response.jsonPath().getInt("freeChampionIds.size()"), equalTo(14));
    	assertThat(response.jsonPath().getInt("freeChampionIdsForNewPlayers.size()"), equalTo(10));
    	assertThat(response.jsonPath().getInt("maxNewPlayerLevel"), equalTo(10));
    }
    
    @Test
    public void serverStatusTest() {
    	response = given().header("X-Riot-Token", Keys.getApikey()).when().get("/lol/status/v3/shard-data");
    	
    	int servicesListSize = response.jsonPath().getInt("services.size()");
    	
    	assertThat(response.getStatusCode(), equalTo(200));
    	assertThat(response.jsonPath().getString("name"), equalTo("Brazil"));
    	
    	for (int i = 0; i < servicesListSize; i++) {
    		assertThat(response.jsonPath().getString("services[" + i + "].status"), equalTo("online"));
    	}
    }
    
}
