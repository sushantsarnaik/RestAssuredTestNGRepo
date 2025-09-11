package com.utilities;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import io.restassured.response.Response;

public class RestBase {
	Logger logger;
	Properties configProperties;
	Response response;
	
	public RestBase() {
		logger=LogManager.getLogger(this.getClass());
		loadProperties();
		baseURI=configProperties.getProperty("BaseURI");
	}
	
	/**
	 *@description method used to load configuration properties from config.properties file
	 */
	public void loadProperties() {
		try {
			logger.info("Into load properties method");
			configProperties = new Properties();
			configProperties.load(new FileInputStream(
					System.getProperty("user.dir") + "/src/test/resources/config.properties"));
			logger.info("Config properties loaded successfully");
		} catch (Exception e) {
			logger.info("Fail to load config properties, Error :" + e.getMessage());
			Assert.fail("Fail to load config properties, Error :" + e.getMessage());
		}
	}
	
	
	/**
	 * @description method specifically used to make a getPosts call
	 */
	public void getPosts() {
		try {
			logger.info("Executing getPosts API");
			response=given()
					 .when()
					 .get(configProperties.getProperty("posts.path"));
			printResponse();
					
		}catch(Exception e) {
			logger.info("Fail to make getPosts call, Error :" + e.getMessage());
			Assert.fail("Fail to make getPosts call, Error :" + e.getMessage());
		}
	}
	
	/**
	 * @description method specifically used to make a getUsers call
	 */
	public void getUsers() {
		try {
			logger.info("Executing getUsers API");
			response=given()
					.log().all()
					.when()
					 	.get(configProperties.getProperty("users.path"));
			printResponse();
					
		}catch(Exception e) {
			logger.info("Fail to make getUsers call, Error :" + e.getMessage());
			Assert.fail("Fail to make getUsers call, Error :" + e.getMessage());
		}
	}
	
	/**
	 * @description method to print response
	 */
	public void printResponse() {
		try {
			logger.info("Response :"+response.prettyPrint());
		}catch(Exception e) {
			logger.info("Fail to print response, Error :" + e.getMessage());
			Assert.fail("Fail to print response, Error :" + e.getMessage());
		}
	}
	
	
	/**
	 * @description method to verify specific field value from response
	 * @param fieldPath
	 * @param expectedValue
	 */
	public void verifyFieldValueInResponse(String fieldPath, String expectedValue) {
		try {
			logger.info("verifing field value from response");
			Assert.assertEquals(response.path(fieldPath), expectedValue, "Expected value missmatched");
		}catch(Exception e) {
			logger.info("Fail to read value from response, Error :" + e.getMessage());
			Assert.fail("Fail to read value from response, Error :" + e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
}
