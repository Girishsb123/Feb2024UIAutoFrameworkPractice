package com.qa.opencart.base;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.AccountsPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.pages.RegisterPage;
import com.qa.opencart.pages.SearchResultsPage;

public class BaseTest {
	
	protected WebDriver driver;
	protected Properties prop;
	DriverFactory df;
	protected LoginPage loginPage;
	protected AccountsPage accPage;
	protected SearchResultsPage searchResultsPage;
	protected ProductInfoPage productInfoPage;
	protected RegisterPage registerPage;
	
	
	protected SoftAssert softAssert;
	
	private static final Logger log = LogManager.getLogger(DriverFactory.class);
	
	@Parameters({"browser","browserversion","testname"})
	@BeforeTest
	public void setup(String browserName,String browserversion,String testname) {
		log.info("browserName" + " : " + " browserVersion" + ":" + "testName");
		df = new DriverFactory();
		prop = df.initProp();
		
		if(browserName!=null) {
			prop.getProperty("browser", browserName);
			prop.setProperty("browserversion", browserversion);
			prop.setProperty("testname", testname);
		}
		
		
		driver = df.initDriver(prop);
		loginPage = new LoginPage(driver);
		softAssert = new SoftAssert();
	}
	
	
	@AfterTest
	public void tearDown() {
		driver.quit();
		log.info("browser is closed....");
	}
	

}
