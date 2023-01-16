package com.lexmark;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.opencsv.CSVWriter;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GLAccountBalancesECX1 {

	
	static String x_csrf_token2, Cookies;
	static String[] outputData;
	static Properties input = new Properties();
	static FileReader readerInput;
	static FileWriter writerOutput;
	static CSVWriter writer;

	@Test	
	public void GLAccountBalancesTestECX1() {
		
		
		TS01GetAccountBalances();
		
	}

    @Test	
	public void TS01GetAccountBalances() {
		
		try {
			readerInput = new FileReader("data/InputData.properties");
			input.load(readerInput);

			/*
			 * writerOutput = new FileWriter("output/GLAccountBalancesECX1.csv", false);
			 * writer = new CSVWriter(writerOutput);
			 */
		   String ledger=input.getProperty("GLAccountBalance.Ledger");
		   String companyCode=input.getProperty("GLAccountBalance.CompanyCode");
		   String ledgerFiscalYear=input.getProperty("GLAccountBalance.LedgerFiscalYear");
		   
			Response response = RestAssured.given()
					.baseUri("https://vhlxkecxci.hec.lexmark.com:44300/sap/opu/odata/sap/FAC_GL_ACCOUNT_BALANCE_SRV")
					.relaxedHTTPSValidation().auth().preemptive()
					.basic(input.getProperty("GLAccountBalance.User"),
							input.getProperty("GLAccountBalance.Password"))
					.header("x-csrf-token", "Fetch")
					.queryParam("$format", "json")
					.queryParam("$top", "99")
				    //.queryParam("$filter", "Ledger eq '0L' and CompanyCode eq '5050' and LedgerFiscalYear eq '2022'&")
					//.when().get("/GL_ACCOUNT_BALANCESet?$select=LedgerFiscalYear, LedgerFiscalPeriod,IsNotPostedTo, CreditAmountInCoCodeCrcy, DebitAmountInCompanyCodeCrcy, BalAmtInCompanyCodeCrcy, AccmltdBalAmtInCoCodeCrcy, CompanyCodeCurrency&$filter=Ledger eq '" +ledger+"' and CompanyCode eq '"+companyCode+"' and LedgerFiscalYear eq '"+ledgerFiscalYear+"'&")
					.when().get("/GL_ACCOUNT_BALANCESet?$select=LedgerFiscalYear, LedgerFiscalPeriod,IsNotPostedTo, CreditAmountInCoCodeCrcy, DebitAmountInCompanyCodeCrcy, BalAmtInCompanyCodeCrcy, AccmltdBalAmtInCoCodeCrcy, CompanyCodeCurrency&$filter=Ledger eq '"+ledger+"' and CompanyCode eq '"+companyCode+"' and LedgerFiscalYear eq '2022'&")
					.then().statusCode(200).statusLine("HTTP/1.1 200 OK")
					.log().all()
					.extract().response();
			
			
		//	Assert.assertTrue(response.jsonPath().get("d.ApproveDRFBRequest.Success"));
		/*
		 * outputData = new String[] { "GL Account Balances", "Passed"};
		 * 
		 * writer.writeNext(outputData, false); writer.close();
		 */
	
		}catch(Exception e) {
			e.printStackTrace();
		}
}
}
