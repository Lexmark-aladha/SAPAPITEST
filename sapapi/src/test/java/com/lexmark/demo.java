/**
 * @author Anup Ladha
 *
 * @date 09/09/2022
 * 
 * @description	Demo class - https://tsapconnect.lexmark.com/irj/portal
 */
package com.lexmark;

import org.apache.commons.io.output.NullOutputStream;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@SuppressWarnings({ "unused" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Listeners({com.lexmark.CustomListener.class, com.lexmark.CustomReporter.class})
public class demo extends Parent {
	static String j_salt, JSESSIONID;

	@Test
	public void TS01GetJSalt() {
		RestAssured.baseURI = "https://tsapconnect.lexmark.com/irj";
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();

		httpRequest.queryParam("saml2", "disabled");
		Response response = httpRequest.request(Method.GET, "/portal");
		Assert.assertEquals(200, response.getStatusCode());
		String[] arrOfStr = response.getBody().asString().split("name=\"j_salt\" value=\"");
		arrOfStr = arrOfStr[1].split("\" />");
		j_salt = arrOfStr[0];
		System.out.println(j_salt);
	}

	@Test
	public void TS02GetJSESSIONID() {
		RestAssured.baseURI = "https://tsapconnect.lexmark.com/irj";
		RequestSpecification httpRequest = RestAssured.given().redirects().follow(false).relaxedHTTPSValidation();

		httpRequest.formParam("login_submit", "on");
		httpRequest.formParam("login_do_redirect", "1");
		httpRequest.formParam("j_salt", j_salt);
		httpRequest.formParam("j_username", "MDMTST9");
		httpRequest.formParam("j_password", "Lexmark123");
		httpRequest.formParam("uidPasswordLogon", "Log On");

		httpRequest.header("Content-Type", "application/x-www-form-urlencoded");
		httpRequest.header("Connection", "keep-alive");
		httpRequest.header("Accept-Encoding", "gzip,deflate,br");
		httpRequest.header("Accept", "*/*");

		httpRequest.queryParam("saml2", "disabled");

		Response response = httpRequest.request(Method.POST, "/portal");
		Assert.assertEquals(302, response.getStatusCode());

		Map<String, String> allCookies = response.getCookies();

		String Cookies = "";
		for (Map.Entry<String, String> entry : allCookies.entrySet()) {
			Cookies = Cookies + entry.getKey() + "=" + entry.getValue() + ";";
		}

		JSESSIONID = response.getCookies().get("JSESSIONID");
		System.out.println("JSESSIONID: " + response.getCookies().get("JSESSIONID"));
	}

	@Test
	public void TS01AuthenticateSAPWebGUI() {
		Response response = RestAssured.given().baseUri("https://ecq.lexmark.com/sap/bc/gui/sap/its")
				.relaxedHTTPSValidation().auth().preemptive().basic("TOSCA1", "Lexmark@1")
				.queryParam("saml2", "disabled").when().get("/webgui").then().statusCode(200)
				.statusLine("HTTP/1.1 200 OK").extract().response();

		if (response.getCookie("SAP_SESSIONID_ECQ_750") != null)
			System.err.println("Authentication Successful!!!");
		else
			Assert.fail("Authentication Failed!!!");
	}

	@Test
	public void TS01ReadCSVData() {
		try {
			FileReader fr = new FileReader("data/DocumentReversal_Input.csv");
			//skip header row
			CSVReader cr = new CSVReaderBuilder(fr).withSkipLines(1).build();

			String[] nextLine;
			int lineNumber = 0;
			while ((nextLine = cr.readNext()) != null) {
				lineNumber++;
				System.out.println("Document Number: " + nextLine[0]);

//			List<String[]> allData = cr.readAll();
//			System.out.println("First: " + allData.get(0));
//			//System.out.println("Size: " + allData.size());
//			
//			for (String[] row : allData) { 
//	            for (String cell : row) { 
//	                System.out.print(cell + "\t"); 
//	            } 
//	            System.out.println();
//	        }
			}
			cr.close();
			fr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}