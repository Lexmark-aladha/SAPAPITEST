/**
 * @author Sohini Paul

 *
 * @date 12/14/2022
 * 
 * @description	Billing Document Reversal Reject - VF11 QA
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

public class BillingDocumentReversalRejectionVF11 {

	static String x_csrf_token4, Cookies, BillingDocRevReqVF11, WiId;
	static String[] outputData;
	static Properties input = new Properties();
	static FileReader readerInput;
	static FileWriter writerOutput;
	static CSVWriter writer;

	@Test
	public void BillingDocumentReversalRejectVF11() {

		TS01GetCsrfTokenAndCookieDataForRequestorVF11();

		TS02CreateBillingDocumentReversalRequestVF11();

		TS03GetCsrfTokenWiIdAndCookieDataForRejectionVF11();

		TS04RejectDocumentReversalRequestVF11();

		TS05WatcherVF11();

	}

	public void TS01GetCsrfTokenAndCookieDataForRequestorVF11() {
		try {

			readerInput = new FileReader("data/InputData.properties");
			input.load(readerInput);

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGV_BILCNC_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("VF11BillingDocumentReversalQA.RequestorUser"),
							input.getProperty("VF11BillingDocumentReversalQA.RequestorPassword"))
					.header("x-csrf-token", "Fetch").queryParam("$format", "json").when().get("/ZG_BILCNC_HEAD_CREATE")
					.then().statusCode(200).statusLine("HTTP/1.1 200 OK").extract().response();

			x_csrf_token4 = response.getHeader("x-csrf-token");

			Cookies = "SAP_SESSIONID_MGQ_750=" + response.getCookie("SAP_SESSIONID_MGQ_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test(dependsOnMethods = { "TS01GetCsrfTokenAndCookieDataForRequestorVF11" })
	public void TS02CreateBillingDocumentReversalRequestVF11() {
		try {
			// byte[] b = Files.readAllBytes(Paths.get("data/payLoad.json"));
			// String payload = new String(b);
			writerOutput = new FileWriter("output/BillingDocumentReversalRejectionVF11.csv", false);
			writer = new CSVWriter(writerOutput);

			Date date = new Date();

			String payload1 = "{\r\n" + "    \"business_area\": \"MPS\",\r\n" + "    \"request_type\": \"01\",\r\n"
					+ "    \"reversal_category\": \"00\",\r\n" + "    \"ritm_no_mps\": \"\",\r\n"
					+ "    \"cnc_reason\": \"Retest adding MFG GEO to all business area\",\r\n"
					+ "    \"corrective_action\": \"Retest adding MFG GEO to all business area\",\r\n"
					+ "    \"Action\": \"SUBM\",\r\n" + "    \"to_Item\": [\r\n" + "        {\r\n"
					+ "            \"item_no\": \"1\",\r\n" + "            \"vbeln_vf\": \""
					+ input.getProperty("VF11BillingDocumentReversal.BillingDoc") + "\",\r\n"
					+ "            \"vbeln_va\": \"" + input.getProperty("VF11BillingDocumentReversal.SalesOrder")
					+ "\",\r\n" + "            \"vbeln_vl\": \"\",\r\n" + "            \"vkorg\": \""
					+ input.getProperty("VF11BillingDocumentReversal.CompanyCode") + "\",\r\n"
					+ "            \"fkdat\": \"/Date(1660867200000)/\",\r\n" + "            \"kunag\": \""
					+ input.getProperty("VF11BillingDocumentReversal.SoldTo") + "\",\r\n"
					+ "            \"name1\": \"Xerox Corporation\",\r\n"
					+ "            \"cancellation_date\": null,\r\n" + "            \"intercmp_bill\": \"\",\r\n"
					+ "            \"invoice_list\": \"\",\r\n" + "            \"faksk\": \"GN\",\r\n"
					+ "            \"fksto\": false,\r\n" + "            \"netwr\": \""
					+ input.getProperty("VF11BillingDocumentReversal.Amount") + "\",\r\n"
					+ "            \"waerk\": \"USD\",\r\n" + "            \"dmbe3\": \"144332.80\",\r\n"
					+ "            \"hwae3\": \"USD\",\r\n" + "            \"error_id\": \"\",\r\n"
					+ "            \"status\": \"Not Cancelled\",\r\n" + "            \"state\": \"Warning\"\r\n"
					+ "        }\r\n" + "    ]\r\n" + "}";

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGV_BILCNC_CREATE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("VF11BillingDocumentReversalQA.RequestorUser"),
							input.getProperty("VF11BillingDocumentReversalQA.RequestorPassword"))
					.header("Accept", "application/json").header("Cookie", Cookies)
					.header("Content-Type", "application/json").header("x-csrf-token", x_csrf_token4).body(payload1)
					.when().post("/ZG_BILCNC_HEAD_CREATE").then().statusCode(201).statusLine("HTTP/1.1 201 Created")
					.extract().response();

			BillingDocRevReqVF11 = response.jsonPath().get("d.request_no");
			outputData = new String[] { "Billing Document Reversal Approve - VF11", "Failed",
					"Billing Document Reversal Request: " + BillingDocRevReqVF11 };
			writer.writeNext(outputData, false);
			Reporter.log("Billing Document Reversal Request Created: " + response.jsonPath().get("d.request_no"));
			System.err.println("Billing Document Reversal Request Created: " + response.jsonPath().get("d.request_no"));

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void TS03GetCsrfTokenWiIdAndCookieDataForRejectionVF11() {
		try {
			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_BILCNC_APPROVE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("VF11BillingDocumentReversalQA.ApproverUser"),
							input.getProperty("VF11BillingDocumentReversalQA.ApproverPassword"))
					.header("Content-Type", "application/json").header("x-csrf-token", "Fetch")
					.queryParam("$format", "json").when().get("/ZG_C_BILCNC_HEAD").then().statusCode(200)
					.statusLine("HTTP/1.1 200 OK").log().all().extract().response();

			x_csrf_token4 = response.getHeader("x-csrf-token");

			Cookies = "SAP_SESSIONID_MGQ_750=" + response.getCookie("SAP_SESSIONID_MGQ_750") + ";sap-usercontext="
					+ response.getCookie("sap-usercontext");

			for (int i = 0; i < response.jsonPath().getList("d.results").size(); i++) {
				String expectedDocRevReqVF11 = response.jsonPath().get("d.results[" + i + "].request_no");

				if (expectedDocRevReqVF11.equals(BillingDocRevReqVF11)) {
					WiId = response.jsonPath().get("d.results[" + i + "].WiId");

					break;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void TS04RejectDocumentReversalRequestVF11() {
		try {

			writerOutput = new FileWriter("output/BillingDocumentReversalRejectionVF11.csv", false);
			writer = new CSVWriter(writerOutput);

			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZGF_BILCNC_APPROVE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("VF11BillingDocumentReversalQA.ApproverUser"),
							input.getProperty("VF11BillingDocumentReversalQA.ApproverPassword"))
					.header("Accept", "application/json").header("Cookie", Cookies)
					.header("x-csrf-token", x_csrf_token4).queryParam("UserComment", "'Test API Reject VF11'")
					// .queryParam("WiId", "'" + WiId + "'")
					.queryParam("request_no", "'" + BillingDocRevReqVF11 + "'").when()
					.post("/ZG_C_BILCNC_HEADAction_reject").then().statusCode(200).statusLine("HTTP/1.1 200 OK")
					.extract().response();

			outputData = new String[] { "Billing Document Reversal Approve - VF11", "Passed",
					"Billing Document Reversal Request: " + BillingDocRevReqVF11 };

			writer.writeNext(outputData, false);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void TS05WatcherVF11() {
		try {

			readerInput = new FileReader("data/InputData.properties");
			input.load(readerInput);

			writerOutput = new FileWriter("output/BillingDocumentReversalRejectionVF11.csv", false);
			writer = new CSVWriter(writerOutput);
			Response response = RestAssured.given()
					.baseUri("https://tsapmobile.lexmark.com/sap/opu/odata/sap/ZG_C_BILCNC_TRC_HEAD_CDS")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("VF11BillingDocumentReversalQA.WatcherUser"),
							input.getProperty("VF11BillingDocumentReversalQA.WatcherPassword"))
					.header("x-csrf-token", "Fetch").queryParam("$format", "json").when()
					.get("/ZG_C_BILCNC_TRC_HEAD('" + BillingDocRevReqVF11 + "')").then().statusCode(200)
					.statusLine("HTTP/1.1 200 OK").extract().response();

			String value = response.jsonPath().get("d.ddtext");

			if (value.equalsIgnoreCase("Rejected")) {

				outputData = new String[] { "Billing Document Reversal Reject - VF11", "Passed & Verified",
						"Billing Document Reversal Request: " + BillingDocRevReqVF11 };
			}
			writer.writeNext(outputData, false);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
