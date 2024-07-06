package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;

import io.qameta.allure.Step;
import io.qameta.allure.Story;

public class LoginPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// By locators: OR
	private By userName = By.id("input-email");
	private By password = By.id("input-password");
	private By loginBtn = By.xpath("//input[@value='Login']");
	private By forgotPwdLink = By.linkText("Forgotten Password");
	private By logo = By.cssSelector("img[title='naveenopencart']");

	private By registerLink = By.linkText("Register");

	// page const...
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(this.driver);

	}

	// page actions/methods:
	@Step("getting login page title test...")
	public String getLoginPageTitle() {
		String title = eleUtil.waitForTitleIs(AppConstants.LOGIN_PAGE_TITLE, AppConstants.SHORT_DEFAUTT_WAIT);
		System.out.println("login page title : " + title);
		return title;
	}

	@Step("getting login page url test...")
	public String getLoginPageUrl() {
		String url = eleUtil.waitForURLContains(AppConstants.LOGIN_PAGE_URL_FRACTION, AppConstants.SHORT_DEFAUTT_WAIT);
		System.out.println("login page url : " + url);
		return url;
	}

	@Step("checking forgot pwd link exist")
	public boolean isForgotPwdLinkExist() {
		return eleUtil.waitForVisibilityOfElement(forgotPwdLink, AppConstants.SHORT_DEFAUTT_WAIT).isDisplayed();
	}

	@Step("checking app logo link exist")
	public boolean isLogoExist() {
		return eleUtil.waitForVisibilityOfElement(logo, AppConstants.SHORT_DEFAUTT_WAIT).isDisplayed();
	}

	@Step("username is : {0} and password {1}")
	public AccountsPage doLogin(String username, String pwd) {
		System.out.println("creds are : " + username + " : " + pwd);
		eleUtil.waitForVisibilityOfElement(userName, AppConstants.MEDIUM_DEFAUTT_WAIT).sendKeys(username);
		eleUtil.dosendKeys(password, pwd);
		eleUtil.doClick(loginBtn);

		return new AccountsPage(driver);
	}

	@Step("navigating to register page")
	public RegisterPage navigateToregisterPage() {
		eleUtil.waitForVisibilityOfElement(registerLink, AppConstants.MEDIUM_DEFAUTT_WAIT).click();
		return new RegisterPage(driver);
	}

}
