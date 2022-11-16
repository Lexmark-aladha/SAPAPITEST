/**
 * @author Anup Ladha
 *
 * @date 
 */

package com.lexmark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Utility {

	public static void writeXLSXFile(String Quote, String fileName, int numberOfCellsToBeUpdated) throws IOException {
		try {
			FileInputStream file = new FileInputStream("data/" + fileName + ".xlsx");

			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Cell cell = null;

			for (int i = 1; i <= numberOfCellsToBeUpdated; i++) {
				XSSFRow sheetrow = sheet.getRow(i);
				if (sheetrow == null) {
					sheetrow = sheet.createRow(i);
				}
				cell = sheetrow.getCell(3);
				if (cell == null) {
					cell = sheetrow.createCell(3);
				}
				cell.setCellValue(Quote);
			}

			file.close();

			FileOutputStream outFile = new FileOutputStream(new File("data/" + fileName + ".xlsx"));
			workbook.write(outFile);
			outFile.close();
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getFileChecksum(MessageDigest digest, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);

		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		;

		fis.close();

		byte[] bytes = digest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static String ConvertFileToByteArray(String fileName) {
		byte[] array = null;
		try {
			array = Files.readAllBytes(Paths.get("data/" + fileName + ".xlsx"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = new String(array);
		return s;
	}

	public static String encodeFileToBase64Binary(String fileName) throws IOException {
		File file = new File(fileName);
		byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		return new String(encoded, StandardCharsets.US_ASCII);
	}

	public static Boolean IsElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean IsAttributePresent(WebDriver driver, By by, String attribute) {
		Boolean result = false;
		try {
			String value = driver.findElement(by).getAttribute(attribute);
			if (value != null) {
				result = true;
			}
		} catch (Exception e) {
			return false;
		}

		return result;
	}

	public static void takeSnapShot(WebDriver webdriver, String fileWithPath) {
		Calendar cal = Calendar.getInstance();
		Date datenow = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy.HH.mm");
		String rprDate = formatter.format(datenow);
		fileWithPath = fileWithPath + rprDate + ".png";
		TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile = new File(fileWithPath);
		try {
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getLargest(int[] a, int total) {
		int temp;
		for (int i = 0; i < total; i++) {
			for (int j = i + 1; j < total; j++) {
				if (a[i] > a[j]) {
					temp = a[i];
					a[i] = a[j];
					a[j] = temp;
				}
			}
		}
		return a[total - 1];
	}
}
