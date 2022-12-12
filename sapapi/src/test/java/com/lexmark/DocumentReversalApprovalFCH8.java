/**
 * @author Sohini Paul

 *
 * @date 11/23/2022
 * 
 * @description	Document Reversal Approve - FCH8 DEV
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
public class DocumentReversalApprovalFCH8 extends Parent {
	static String x_csrf_token1, Cookies, DocRevReqFCH8, WiId;
	static String[] outputData;
	static Properties input = new Properties();
	static FileReader readerInput;
	static FileWriter writerOutput;
	static CSVWriter writer;

	@Test
	public void DocumentReversalApproveFCH8() {
		TS01GetCsrfTokenAndCookieDataForRequestorFCH8();
		TS02CreateDocumentReversalRequestFCH8();
		TS03GetCsrfTokenWiIdAndCookieDataForApproverFCH8();
		// TS04ApproveDocumentReversalRequestFCH8();
	}

	@Test
	public void TS01GetCsrfTokenAndCookieDataForRequestorFCH8() {
		try {
			readerInput = new FileReader("data/InputData.properties");
			input.load(readerInput);

			Response response = RestAssured.given()
					.baseUri("https://vhlxkmgdci.hec.lexmark.com:44300/sap/opu/odata/sap/ZGF_DOCREVCH_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("FCH8DocumentReversal.RequestorUser"),
							input.getProperty("FCH8DocumentReversal.RequestorPassword"))
					.header("x-csrf-token", "Fetch").queryParam("$format", "json").when().get("/ZGCDS_C_DocRevCH_H")
					.then().statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();

			x_csrf_token1 = response.getHeader("x-csrf-token");

			Cookies = "SAP_SESSIONID_MGD_750=" + response.getCookie("SAP_SESSIONID_MGD_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = { "TS01GetCsrfTokenAndCookieDataForRequestorFCH8" })
	public void TS02CreateDocumentReversalRequestFCH8() {
		try {
			/*
			 * byte[] b = Files.readAllBytes(Paths.get("data/payload1.json")); String
			 * payload1 = new String(b);
			 */

			writerOutput = new FileWriter("output/DocumentReversalApprovalFCH8.csv", false);
			writer = new CSVWriter(writerOutput);
			Date date = new Date();

			String payload = "{\"zz_drch_no\":\"\",\"zz_comment\":\"Test\",\"budat\":\"/Date(" + date.getTime()
					+ ")/\",\"to_Items\":[{\"zz_drch_no\":\"\",\"zz_drch_item\":\"\",\"zbukr\":\""
					+ input.getProperty("FCH8DocumentReversal.CompanyCode") + "\",\"hbkid\":\""
					+ input.getProperty("FCH8DocumentReversal.HouseBank") + "\",\"hktid\":\""
					+ input.getProperty("FCH8DocumentReversal.AccountNo") + "\",\"chect\":\""
					+ input.getProperty("FCH8DocumentReversal.CheckNo") + "\",\"vblnr\":\""
					+ input.getProperty("FCH8DocumentReversal.PaymentDoc") + "\",\"gjahr\":\""
					+ input.getProperty("FCH8DocumentReversal.FiscalYear") + "\",\"rwbtr\":\""
					+ input.getProperty("FCH8DocumentReversal.Amount") + "\",\"waers\":\"USD\",\"monat\":\""
					+ input.getProperty("FCH8DocumentReversal.PostingPeriod") + "\",\"stgrd\":\""
					+ input.getProperty("FCH8DocumentReversal.ReversalReason") + "\",\"voidr\":\""
					+ input.getProperty("FCH8DocumentReversal.VoidReason") + "\",\"zaldt\":\""
					+ input.getProperty("FCH8DocumentReversal.PaymentDate") + "\",\"znme1\":\""
					+ input.getProperty("FCH8DocumentReversal.PayeeName")
					+ "\",\"message\":\"\"}],\"Action\":\"SUBM\"}";

			Response response = RestAssured.given()
					.baseUri("https://vhlxkmgdci.hec.lexmark.com:44300/sap/opu/odata/sap/ZGF_DOCREVCH_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("FCH8DocumentReversal.RequestorUser"),
							input.getProperty("FCH8DocumentReversal.RequestorPassword"))
					.header("Accept", "application/json").header("Cookie", Cookies)
					.header("x-csrf-token", x_csrf_token1).header("Content-Type", "application/json").body(payload)
					.when().post("/ZGCDS_C_DocRevCH_H").then().statusCode(201).statusLine("HTTP/1.1 201 Created")
					.extract().response();

			DocRevReqFCH8 = response.jsonPath().get("d.zz_drch_no");
			System.out.println("Doc " + DocRevReqFCH8);

			outputData = new String[] { "Document Reversal Approve -FCH8", "Failed",
					"Document Reversal Request: " + DocRevReqFCH8 };

			writer.writeNext(outputData, false);

			Reporter.log("Document Reversal Request Created FCH8: " + response.jsonPath().get("d.zz_drch_no"));
			System.err.println("Document Reversal Request Created FCH8: " + response.jsonPath().get("d.zz_drch_no"));

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = { "TS02CreateDocumentReversalRequestFCH8" })
	public void TS03GetCsrfTokenWiIdAndCookieDataForApproverFCH8() {
		try {
			Response response = RestAssured.given()
					.baseUri("https://vhlxkmgdci.hec.lexmark.com:44300/sap/opu/odata/sap/ZG_C_DRCH_HEAD_CDS")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("FCH8DocumentReversal.ApproverUser"),
							input.getProperty("FCH8DocumentReversal.ApproverPassword"))
					.header("x-csrf-token", "Fetch").queryParam("$format", "json").when().get("/ZG_C_DRCH_HEAD").then()
					.statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();

			x_csrf_token1 = response.getHeader("x-csrf-token");
			Cookies = "SAP_SESSIONID_MGD_750=" + response.getCookie("SAP_SESSIONID_MGD_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext") + ";MYSAPSSO2=" + response.getCookie("MYSAPSSO2");

			for (int i = 0; i < response.jsonPath().getList("d.results").size(); i++) {
				String expectedDocRevReqFCH8 = response.jsonPath().get("d.results[" + i + "].zz_drch_no");

				if (expectedDocRevReqFCH8.equals(DocRevReqFCH8)) {
					WiId = response.jsonPath().get("d.results[" + i + "].WiId");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Test
	 * 
	 * public void TS04ApproveDocumentReversalRequestFCH8() { try { writerOutput =
	 * new FileWriter("output/DocumentReversalApprove.csv", false); writer = new
	 * CSVWriter(writerOutput); Response response = RestAssured.given() .baseUri(
	 * "https://vhlxkmgdci.hec.lexmark.com:44300/sap/opu/odata/sap/ZG_C_DRCH_HEAD_CDS")
	 * .relaxedHTTPSValidation().auth().preemptive()
	 * .basic(input.getProperty("FCH8DocumentReversal.ApproverUser"),
	 * input.getProperty("FCH8DocumentReversal.ApproverPassword")) .header("Accept",
	 * "application/json").header("Cookie", Cookies) .header("x-csrf-token",
	 * x_csrf_token1).queryParam("ApproverComment", "'Test API Approve'")
	 * .queryParam("WiId", "'" + WiId + "'").queryParam("ZZ_DRCH_NO", "'" +
	 * DocRevReqFCH8 + "'") .when()
	 * .post("/ZG_C_DRCH_HEADAction_approve").then().statusCode(200).
	 * statusLine("HTTP/1.1 200 OK") .log().all() .extract().response();
	 * Assert.assertTrue(response.jsonPath().get("d.ApproveDRFBRequest.Success"));
	 * outputData = new String[] { " Document Reversal Approve - FCH8", "Passed",
	 * "Document Reversal Request: " + DocRevReqFCH8 }; writer.writeNext(outputData,
	 * false); writer.close(); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 */
}
