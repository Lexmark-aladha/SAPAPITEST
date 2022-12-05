/**
 * @author Sohini Paul

 *
 * @date 11/29/2022
 * 
 * @description	Document Reversal Approve - FCH8 QA
 */
package com.lexmark;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.opencsv.CSVWriter;

import org.junit.Assert;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings({ "unused" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DocumentReversalApprovalQAFCH8 {

	static String x_csrf_token2, Cookies, DocRevReqFCH8QA, WiId;
	static String[] outputData;
	static Properties input = new Properties();
	static FileReader readerInput;
	static FileWriter writerOutput;
	static CSVWriter writer;

	@Test
	public void DocumentReversalApproveQAFCH8() {

		TS01GetCsrfTokenAndCookieDataForRequestorQAFCH8();
		TS02CreateDocumentReversalRequestQAFCH8();
		TS03GetCsrfTokenWiIdAndCookieDataForApproverQAFCH8();
		TS04ApproveDocumentReversalRequestQAFCH8();

	}

	@Test
	public void TS01GetCsrfTokenAndCookieDataForRequestorQAFCH8() {
		try {

			readerInput = new FileReader("data/InputData.properties");
			input.load(readerInput);

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVCH_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("FCH8DocumentReversalQA.RequestorUser"),
							input.getProperty("FCH8DocumentReversalQA.RequestorPassword"))
					.header("x-csrf-token", "Fetch").queryParam("$format", "json").when().get("/ZGCDS_C_DocRevCH_H")
					.then().statusCode(200).statusLine("HTTP/1.1 200 OK").log().all().extract().response();

			x_csrf_token2 = response.getHeader("x-csrf-token");

			Cookies = "SAP_SESSIONID_MGQ_750=" + response.getCookie("SAP_SESSIONID_MGQ_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test(dependsOnMethods = { "TS01GetCsrfTokenAndCookieDataForRequestorQAFCH8" })
	public void TS02CreateDocumentReversalRequestQAFCH8() {
		try {
			// byte[] b = Files.readAllBytes(Paths.get("data/payLoad.json"));
			// String payload = new String(b);
			writerOutput = new FileWriter("output/DocumentReversalApprovalFCH8QA.csv", false);
			writer = new CSVWriter(writerOutput);

			System.out.println("x_csrf_token2  in post   " + x_csrf_token2);
			System.out.println("Cookies in Post    " + Cookies);
			Date date = new Date();

			String payload1 = "{\r\n" + "    \"zz_drch_no\": \"\",\r\n" + "    \"zz_comment\": \"Test\",\r\n"
					+ "    \"budat\": \"/Date(" + date.getTime() + ")/\",\r\n" + "    \"to_Items\": [\r\n"
					+ "        {\r\n" + "            \"zz_drch_no\": \"\",\r\n"
					+ "            \"zz_drch_item\": \"\",\r\n" + "            \"zbukr\": \""
					+ input.getProperty("FCH8DocumentReversalQA.CompanyCode") + "\",\r\n" + "            \"hbkid\": \""
					+ input.getProperty("FCH8DocumentReversalQA.HouseBank") + "\",\r\n" + "            \"hktid\": \""
					+ input.getProperty("FCH8DocumentReversalQA.AccountNo") + "\",\r\n" + "            \"chect\": \""
					+ input.getProperty("FCH8DocumentReversalQA.CheckNo") + "\",\r\n" + "            \"vblnr\": \""
					+ input.getProperty("FCH8DocumentReversalQA.PaymentDoc") + "\",\r\n"
					+ "            \"gjahr\": \"2022\",\r\n" + "            \"rwbtr\": \"-405.00\",\r\n"
					+ "            \"waers\": \"USD\",\r\n" + "            \"monat\": \"12\",\r\n"
					+ "            \"stgrd\": \"02\",\r\n" + "            \"voidr\": \"10\",\r\n"
					+ "            \"zaldt\": \"2022-07-22T00:00:00\",\r\n" + "            \"znme1\": \""
					+ input.getProperty("FCH8DocumentReversalQA.PayeeName") + "\",\r\n"
					+ "            \"message\": \"\"\r\n" + "        }\r\n" + "    ],\r\n"
					+ "    \"Action\": \"SUBM\"\r\n" + "}";

			/*
			 * String jsonPayload =
			 * "{\"zz_drch_no\":\"\",\"zz_comment\":\"Test\",\"budat\":\"/Date(" +
			 * date.getTime() +
			 * ")/\",\"to_Items\":[{\"zz_drch_no\":\"\",\"zz_drch_item\":\"\",\"zbukr\":\""
			 * + input.getProperty("FCH8DocumentReversalQA.CompanyCode") + "\",\"hbkid\":\""
			 * + input.getProperty("FCH8DocumentReversalQA.HouseBank") + "\",\"hktid\":\"" +
			 * input.getProperty("FCH8DocumentReversalQA.AccountNo") + "\",\"chect\":\"" +
			 * input.getProperty("FCH8DocumentReversalQA.CheckNo") + "\",\"vblnr\":\"" +
			 * input.getProperty("FCH8DocumentReversalQA.PaymentDoc") + "\",\"gjahr\":\"" +
			 * input.getProperty("FCH8DocumentReversalQA.FiscalYear") + "\",\"rwbtr\":\"" +
			 * input.getProperty("FCH8DocumentReversalQA.Amount") +
			 * "\",\"waers\":\"USD\",\"monat\":\"" +
			 * input.getProperty("FCH8DocumentReversalQA.PostingPeriod") + "\",\"stgrd\":\""
			 * + input.getProperty("FCH8DocumentReversalQA.ReversalReason") +
			 * "\",\"voidr\":\"" + input.getProperty("FCH8DocumentReversalQA.VoidReason") +
			 * "\",\"zaldt\":\"" + input.getProperty("FCH8DocumentReversalQA.PaymentDate") +
			 * "\",\"znme1\":\"" + input.getProperty("FCH8DocumentReversalQA.PayeeName") +
			 * "\",\"message\":\"\"}],\"Action\":\"SUBM\"}";
			 */

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVCH_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("FCH8DocumentReversalQA.RequestorUser"),
							input.getProperty("FCH8DocumentReversalQA.RequestorPassword"))
					.header("Accept", "application/json").header("Cookie", Cookies)
					.header("Content-Type", "application/json").header("x-csrf-token", x_csrf_token2).body(payload1)
					.when().post("/ZGCDS_C_DocRevCH_H").then().statusCode(201).statusLine("HTTP/1.1 201 Created").log()
					.all().extract().response();

			DocRevReqFCH8QA = response.jsonPath().get("d.zz_drch_no");
			outputData = new String[] { "Document Reversal Approve - FCH8", "Failed",
					"Document Reversal Request: " + DocRevReqFCH8QA };
			writer.writeNext(outputData, false);
			Reporter.log("Document Reversal Request Created: " + response.jsonPath().get("d.zz_drch_no"));
			System.err.println("Document Reversal Request Created: " + response.jsonPath().get("d.zz_drch_no"));

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test(dependsOnMethods = { "TS02CreateDocumentReversalRequestQAFCH8" })
	public void TS03GetCsrfTokenWiIdAndCookieDataForApproverQAFCH8() {
		try {
			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZG_C_DRCH_HEAD_CDS")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("FCH8DocumentReversalQA.ApproverUser"),
							input.getProperty("FCH8DocumentReversalQA.ApproverPassword"))
					.header("Content-Type", "application/json").header("x-csrf-token", "Fetch")
					.queryParam("$format", "json").when().get("/ZG_C_DRCH_HEAD").then().statusCode(200)
					.statusLine("HTTP/1.1 200 OK").log().all().extract().response();

			x_csrf_token2 = response.getHeader("x-csrf-token");

			Cookies = "SAP_SESSIONID_MGQ_750=" + response.getCookie("SAP_SESSIONID_MGQ_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext");
			// + ";MYSAPSSO2=" + response.getCookie("MYSAPSSO2");

			for (int i = 0; i < response.jsonPath().getList("d.results").size(); i++) {
				String expectedDocRevReqFCH8 = response.jsonPath().get("d.results[" + i + "].zz_drch_no");

				if (expectedDocRevReqFCH8.equals(DocRevReqFCH8QA)) {
					WiId = response.jsonPath().get("d.results[" + i + "].WiId");
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = { "TS03GetCsrfTokenWiIdAndCookieDataForApproverQAFCH8" })
	public void TS04ApproveDocumentReversalRequestQAFCH8() {
		try {
			writerOutput = new FileWriter("output/DocumentReversalApprovalFCH8QA.csv", false);
			writer = new CSVWriter(writerOutput);

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZG_C_DRCH_HEAD_CDS")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("FCH8DocumentReversalQA.ApproverUser"),
							input.getProperty("FCH8DocumentReversalQA.ApproverPassword"))
					.header("Accept", "application/json").header("Cookie", Cookies)
					.header("x-csrf-token", x_csrf_token2)
					// .queryParam("ApproverComment", "'Test API Approve'")
					// .queryParam("WiId", "'" + WiId + "'")
					.queryParam("zz_drch_no", "'" + DocRevReqFCH8QA + "'").when().post("/ZG_C_DRCH_HEADAction_approve")
					.then().statusCode(200).statusLine("HTTP/1.1 200 OK").log().all().extract().response();
			// Assert.assertTrue(response.jsonPath().get("d.ApproveDRFBRequest.Success"));
			outputData = new String[] { "Document Reversal Approve - FCH8", "Passed",
					"Document Reversal Request: " + DocRevReqFCH8QA };
			writer.writeNext(outputData, false);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
