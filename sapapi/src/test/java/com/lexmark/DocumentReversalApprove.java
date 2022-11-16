/**
 * @author Anup Ladha
 *
 * @date 09/26/2022
 * 
 * @description	TMTC0006826 Document Reversal Approve - FB08 & FBRA
 */
package com.lexmark;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.junit.Assert;
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
//@Listeners({com.lexmark.CustomListener.class, com.lexmark.CustomReporter.class})
public class DocumentReversalApprove extends Parent {
	static String x_csrf_token, Cookies, DocRevReq, WiId;
	static String[] outputData;
	static Properties input = new Properties();
	static FileReader readerInput;
	static FileWriter writerOutput;
	static CSVWriter writer;
	
	@Test
	public void DocumentReversalApproveFB08() {
		TS01GetCsrfTokenAndCookieDataForRequestor();
		TS02CreateDocumentReversalRequest();
		TS03GetCsrfTokenWiIdAndCookieDataForApprover();
		TS04ApproveDocumentReversalRequest();
	}

	@Test
	public void TS01GetCsrfTokenAndCookieDataForRequestor() {
		try {
			// String[] cmd = {"cmd.exe", "/c", "copy", System.getProperty("user.dir") +
			// "\\output\\*.csv", System.getProperty("user.dir") + "\\output\\Report.csv"};
			// Process process = new ProcessBuilder(cmd).start();
			// process.waitFor();
			
			readerInput = new FileReader("data/InputData.properties");
			input.load(readerInput);

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVFI_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("DocumentReversal.RequestorUser"),
							input.getProperty("DocumentReversal.RequestorPassword"))
					.header("x-csrf-token", "Fetch").queryParam("$format", "json").when().get("/ZGCDS_C_DocRevFI_H")
					.then().statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();

			x_csrf_token = response.getHeader("x-csrf-token");
			Cookies = "SAP_SESSIONID_MGQ_750=" + response.getCookie("SAP_SESSIONID_MGQ_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(dependsOnMethods = { "TS01GetCsrfTokenAndCookieDataForRequestor" })
	public void TS02CreateDocumentReversalRequest() {
		try {
			// byte[] b = Files.readAllBytes(Paths.get("data/payLoad.json"));
			// String payload = new String(b);
			writerOutput = new FileWriter("output/DocumentReversalApprove.csv", false);
			writer = new CSVWriter(writerOutput);

			Date date = new Date();
			String jsonPayload = "{\"zz_drfb_no\":\"\",\"crdate\":\"/Date(" + date.getTime() + ")/\",\"ernam\":\""
					+ input.getProperty("DocumentReversal.RequestorUser") + "\",\"bukrs\":\""
					+ input.getProperty("DocumentReversal.CompanyCode") + "\",\"gjahr\":\""
					+ input.getProperty("DocumentReversal.FiscalYear") + "\",\"monat\":\"00\",\"budat\":\"/Date("
					+ date.getTime()
					+ ")/\",\"zz_comment\":\"API Automation\",\"Action\":\"SUBM\",\"to_Items\":[{\"zz_drfb_no\":\"\",\"zz_drfb_item\":\"000001\",\"belnr\":\""
					+ input.getProperty("DocumentReversal.DocumentNumber") + "\",\"bukrs\":\""
					+ input.getProperty("DocumentReversal.CompanyCode") + "\",\"gjahr\":\""
					+ input.getProperty("DocumentReversal.FiscalYear") + "\",\"tcode\":\""
					+ input.getProperty("DocumentReversal.ReversalTCode")
					+ "\",\"rev_cat\":\"Double Posting\",\"stgrd\":\"02\",\"res_rev\":\"X\",\"augbl\":\"\",\"auggj\":\"0000\",\"augbk\":\"\"}]}";
			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVFI_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("DocumentReversal.RequestorUser"),
							input.getProperty("DocumentReversal.RequestorPassword"))
					.header("Accept", "application/json").header("Cookie", Cookies)
					.header("Content-Type", "application/json").header("x-csrf-token", x_csrf_token).body(jsonPayload)
					.when().post("/ZGCDS_C_DocRevFI_H").then().statusCode(201).statusLine("HTTP/1.1 201 Created")
					.extract().response();

			DocRevReq = response.jsonPath().get("d.zz_drfb_no");
			outputData = new String[] { "TMTC0006826 Document Reversal Approve - FB08 & FBRA", "Failed",
					"Document Reversal Request: " + DocRevReq };
			writer.writeNext(outputData, false);
			Reporter.log("Document Reversal Request Created: " + response.jsonPath().get("d.zz_drfb_no"));
			System.err.println("Document Reversal Request Created: " + response.jsonPath().get("d.zz_drfb_no"));
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(dependsOnMethods = { "TS02CreateDocumentReversalRequest" })
	public void TS03GetCsrfTokenWiIdAndCookieDataForApprover() {
		try {
			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVFI_APPR_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("DocumentReversal.ApproverUser"),
							input.getProperty("DocumentReversal.ApproverPassword"))
					.header("x-csrf-token", "Fetch").queryParam("$format", "json").when().get("/HeaderSet").then()
					.statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();

			x_csrf_token = response.getHeader("x-csrf-token");
			Cookies = "SAP_SESSIONID_MGQ_750=" + response.getCookie("SAP_SESSIONID_MGQ_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext");
			
			for (int i = 0; i < response.jsonPath().getList("d.results").size(); i++) {
				String expectedDocRevReq = response.jsonPath().get("d.results[" + i + "].ZzDrfbNo");
				if (expectedDocRevReq.equals(DocRevReq)) {
					WiId = response.jsonPath().get("d.results[" + i + "].WiId");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(dependsOnMethods = { "TS03GetCsrfTokenWiIdAndCookieDataForApprover" })
	public void TS04ApproveDocumentReversalRequest() {
		try {
			writerOutput = new FileWriter("output/DocumentReversalApprove.csv", false);
			writer = new CSVWriter(writerOutput);
			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_DOCREVFI_APPR_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("DocumentReversal.ApproverUser"),
							input.getProperty("DocumentReversal.ApproverPassword"))
					.header("Accept", "application/json").header("Cookie", Cookies).header("x-csrf-token", x_csrf_token)
					.queryParam("ApproverComment", "'Test API Approve'").queryParam("WiId", "'" + WiId + "'")
					.queryParam("ZZ_DRFB_NO", "'" + DocRevReq + "'").when().post("/ApproveDRFBRequest").then()
					.statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();
			Assert.assertTrue(response.jsonPath().get("d.ApproveDRFBRequest.Success"));
			outputData = new String[] { "TMTC0006826 Document Reversal Approve - FB08 & FBRA", "Passed",
					"Document Reversal Request: " + DocRevReq };
			writer.writeNext(outputData, false);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}