/**
 * @author Anup Ladha
 *
 * @date 09/16/2022
 * 
 * @description	Demo class - Document Reversal - https://tsapmobile.lexmark.com
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
import io.restassured.config.EncoderConfig;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

@SuppressWarnings({ "unused" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Listeners({com.lexmark.CustomListener.class, com.lexmark.CustomReporter.class})
public class DocumentReversal2 extends Parent {
	@Test
	public void TS01GetDocRevs() {
		try {
			FileWriter myWriter = new FileWriter("output/response.txt");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			RestAssured.baseURI = "https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVFI_CREATE_SRV";
			RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation().auth().preemptive()
					.basic("LRTEST58", "Lex@2020");
			// RequestSpecification httpRequest =
			// RestAssured.given().auth().basic("LRTEST58",
			// "Lex@2020").config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")));
			// httpRequest.header("Accept", "*/*");
			// httpRequest.header("Accept-Encoding", "gzip, deflate, br");
			// httpRequest.header("Connection", "keep-alive");
			// httpRequest.header("User-Agent", "PostmanRuntime/7.29.2");

			Response response = httpRequest.request(Method.GET, "/ZGCDS_C_DocRevFI_H");
			Assert.assertEquals(200, response.getStatusCode());
			new FileWriter("output/response.txt", false).close();
			myWriter.write(dtf.format(now) + "\n" + response.getBody().asPrettyString());
			myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}