package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.qa.opencart.exception.FrameworkException;

public class DriverFactory {

	WebDriver driver;
	Properties prop;
	OptionsManager optionsManager;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();
	
	private static final Logger log = LogManager.getLogger(DriverFactory.class);

	public WebDriver initDriver(Properties prop) {

		String browserName = prop.getProperty("browser");
//		String browserName = System.getProperty("browser");

//		System.out.println("browser name is : " + browserName);
		log.info("browser name is: " + browserName);

		optionsManager = new OptionsManager(prop);

		switch (browserName.trim().toLowerCase()) {
		case "chrome":
			 log.info("Runing it on chrome browser...");
			driver = new ChromeDriver(optionsManager.getChromeOptions());

			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// run it on grid
				log.info("Runing it on remote machine...");
				initRemoteDriver(browserName);
				// run it on local
				log.info("Runing it on local machine...");
			} else {
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}
			break;
		case "firefox":
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// run it on grid
				initRemoteDriver(browserName);
				// run it on local
			} else {
				tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			}

			break;
		case "edge":
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// run it on grid
				initRemoteDriver(browserName);
				// run it on local
			} else {
				tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			}

			break;
		case "safari":
			System.out.println("safari is launched");
			break;

		default:
//			System.out.println("Plz pass the right browser : " + browserName);
			log.warn("please pass the right browser name...." + browserName);
			throw new FrameworkException("INVALID BROWSER : " + browserName);
		}

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().get(prop.getProperty("url"));

		return getDriver();
	}

	/*
	 * run tests on grid
	 * 
	 * @param browserName
	 */

	private void initRemoteDriver(String browserName) {
//		System.out.println("Running tests on GRID with browser: " + browserName);
		log.info("Running tests on GRID with browser: " + browserName);

		try {
			switch (browserName.toLowerCase().trim()) {
			case "chrome":
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
				break;
			case "firefox":
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));
				break;
			case "edge":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));
				break;

			default:
				System.out.println("wrong browser info..can not run on grid remote machine....");
				break;
			}
		} catch (MalformedURLException e) {

		}

	}

	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	public Properties initProp() {

		// mvn clean install -Denv="qa"
		// mvn clean install
		FileInputStream ip = null;
		prop = new Properties();

		String envName = System.getProperty("env");
//		System.out.println("env name is: " + envName);
		log.info("env name is: " + envName);

		try {
			if (envName == null) {
//				System.out.println("your env is null...hence running tests on QA env...");
				log.warn("your env is null...hence running tests on QA env...");
				ip = new FileInputStream(".\\src\\test\\resources\\config\\config.qa.properties");
				log.info(ip);
			}

			else {
				switch (envName.toLowerCase().trim()) {
				case "qa":
					ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
					break;
				case "dev":
					ip = new FileInputStream("./src/test/resources/config/config.dev.properties");
					break;
				case "stage":
					ip = new FileInputStream("./src/test/resources/config/config.stage.properties");
					break;
				case "uat":
					ip = new FileInputStream("./src/test/resources/config/config.uat.properties");
					break;
				case "prod":
					ip = new FileInputStream("./src/test/resources/config/config.prod.properties");
					break;

				default:
//					System.out.println("please pass the right env name..." + envName);
					log.error("wrong env name..." + envName);
					throw new FrameworkException("Wrong Env Name: " + envName);
				}
			}
		} catch (FileNotFoundException e) {

		}

		try {
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;

	}

	/**
	 * take screenshot
	 */
	public static String getScreenshot(String methodName) {

		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

		String path = System.getProperty("user.dir") + "/screenshot/" + methodName + "_" + System.currentTimeMillis()
				+ ".png";

		File destination = new File(path);

		try {
			FileHandler.copy(srcFile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;
	}

}
