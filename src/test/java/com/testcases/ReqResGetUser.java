package com.testcases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.utilities.RestBase;


public class ReqResGetUser{
	RestBase base;
	
	@BeforeClass
	public void init() {
		base=new RestBase();
	}
	
	@Test
	public void sampleTest() {
		base.getUsers();
		base.verifyFieldValueInResponse("[0].username", "Brettt");
	}
	

}
