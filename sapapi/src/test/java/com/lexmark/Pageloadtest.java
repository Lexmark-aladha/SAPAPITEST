package com.lexmark;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Pageloadtest {
	static WebDriver driver;

	@Test
	public void pageLoadTest() {
		WebDriverManager.chromedriver().setup();
		/*
		 * WebDriverManager.firefoxdriver().setup();
		 * WebDriverManager.edgedriver().setup();
		 * WebDriverManager.operadriver().setup();
		 * WebDriverManager.chromiumdriver().setup();
		 * WebDriverManager.iedriver().setup(); 
		 * WebDriverManager.safaridriver().setup();
		 */
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.msn.com/");
		System.out.println("PageLoad Test: " + com.lexmark.Utility.waitForPageLoad(driver, 40));
		driver.quit();
	}
}
