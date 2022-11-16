/**
 * @author Anup Ladha
 *
 * @date 09/19/2022
 * 
 * @description	Demo class - Document Reversal - https://tsapmobile.lexmark.com
 */
package com.lexmark;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang3.StringUtils;
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
import io.restassured.response.ResponseOptions;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.io.*;

@SuppressWarnings({ "unused" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Listeners({com.lexmark.CustomListener.class, com.lexmark.CustomReporter.class})
public class DocumentReversal3 extends Parent {
	static String x_csrf_token;

	@Test
	public void TS01GetDocRevs() {
		try {
			FileWriter myWriter = new FileWriter("output/response.txt");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			int[] DocRevs = new int[100];
			String[] arrOfStr;
			new FileWriter("output/response.txt", false).close();

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVFI_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive().basic("LRTEST58", "Lex@2020")
					.header("x-csrf-token", "fetch").when().get("/ZGCDS_C_DocRevFI_H").then().statusCode(200)
					.statusLine("HTTP/1.1 200 OK").extract().response();

			String responseBody = response.getBody().asString();
			int noOfDocRevs = StringUtils.countMatches(responseBody,
					"dataservices/scheme\"/><link href=\"ZGCDS_C_DocRevFI_H('");
			for (int i = 0; i < noOfDocRevs; i++) {
				arrOfStr = responseBody.split("dataservices/scheme\"/><link href=\"ZGCDS_C_DocRevFI_H\\('");
				arrOfStr = arrOfStr[1].split("'\\)\" rel=\"edit\"");
				DocRevs[i] = Integer.parseInt(arrOfStr[0]);
				responseBody = responseBody
						.replaceFirst(Pattern.quote("dataservices/scheme\"/><link href=\"ZGCDS_C_DocRevFI_H('"), "");
			}

			System.err.println(
					"Latest Document Reversal Number: " + com.lexmark.Utility.getLargest(DocRevs, noOfDocRevs));

			myWriter.write(dtf.format(now) + "\n" + response.getBody().asPrettyString());
			myWriter.close();
			x_csrf_token = response.getHeader("x-csrf-token");
			System.out.println(x_csrf_token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}