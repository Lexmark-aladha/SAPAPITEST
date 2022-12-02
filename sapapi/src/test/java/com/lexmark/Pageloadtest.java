package com.lexmark;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

@SuppressWarnings({ "deprecation" })
public class Pageloadtest {
	static WebDriver driver;

	public static boolean waitForPageLoad(WebDriver driver, int timeout) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		boolean flag = wait.until(pageLoadCondition);
		return flag;
	}

	@Test
	public void pageLoadTest() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.msn.com/");
		System.out.println("PageLoad Test: " + waitForPageLoad(driver, 40));
		//driver.close();
	}
}
