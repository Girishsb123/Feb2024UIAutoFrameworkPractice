package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.exception.FrameworkException;
import com.qa.opencart.pages.LoginPage;

import io.qameta.allure.Step;

public class ElementUtil {

	private WebDriver driver;// 123
	private JavaScriptUtil jsUtil;
	
	private static final Logger log = LogManager.getLogger(ElementUtil.class);

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
	}

	public By getBy(String locatorType, String locatorValue) {

		By by = null;

		switch (locatorType.toLowerCase().trim()) {
		case "id":
			by = By.id(locatorValue);
			break;
		case "name":
			by = By.name(locatorValue);
			break;
		case "class":
			by = By.className(locatorValue);
			break;
		case "xpath":
			by = By.xpath(locatorValue);
			break;
		case "css":
			by = By.cssSelector(locatorValue);
			break;
		case "partialLinktext":
			by = By.partialLinkText(locatorValue);
			break;
		case "linktext":
			by = By.linkText(locatorValue);
			break;
		case "tag":
			by = By.tagName(locatorValue);
			break;

		default:
			System.out.println("Wrong locator type is passed : " + locatorType);
			throw new FrameworkException("WRONG LOCATOR TYPE");
		}

		return by;
	}
	
	private void logLocator(By locator) {
		log.info("locator : "+locator);
	}

	private void logLocator(By locator,String value) {
		log.info("locator : "+locator+ "----value-----" +value);
	}
	
	// locatorType = "id", locatorValue = "input-email", value = "tom@gmail.com"
	public void dosendKeys(String locatorType, String locatorValue, String value) {
		getElement(locatorType, locatorValue).sendKeys(value);
	}

	@Step("entering value {1} to element {0}")
	public void dosendKeys(By locator, String value) {
		logLocator(locator,value);
		getElement(locator).sendKeys(value);

	}

	@Step("getting element {0}")
	public WebElement getElement(By locator) {
		logLocator(locator);
		return driver.findElement(locator);
	}

	@Step("getting element text {0}")
	public String doElementgetText(By locator) {
		return getElement(locator).getText();
	}

	@Step("getting element text {0} and {1}")
	public String doElementgetText(String locatorType, String locatorValue) {
		return getElement(locatorType, locatorValue).getText();
	}

	@Step("getting element attribute {0} and attrName {1}")
	public String doElementGetAttribute(By locator, String attrName) {
		return getElement(locator).getAttribute(attrName);
	}

	public WebElement getElement(String locatorType, String locatorValue) {
		return driver.findElement(getBy(locatorType, locatorValue));
	}

	public void doClick(String locatorType, String locatorValue) {
		getElement(locatorType, locatorValue).click();

	}

	@Step("clicking on element : {0}")
	public void doClick(By locator) {
		logLocator(locator);
		getElement(locator).click();

	}

	public boolean checkSingleElementPresent(By locator) {
		return driver.findElements(locator).size() == 1 ? true : false;
	}

	public boolean checkElementPresent(By locator) {
		return driver.findElements(locator).size() >= 1 ? true : false;
	}

	public boolean checkElementPresent(By locator, int totalElements) {
		return driver.findElements(locator).size() >= totalElements ? true : false;
	}

	// WAF : capture the text of all the page links and return List<String>.
	public List<String> getElementsTextList(By locator) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleTextList = new ArrayList<String>();// pc = 0
		for (WebElement e : eleList) {
			String text = e.getText();

			if (text.length() != 0) {
				eleTextList.add(text);
			}

		}

		return eleTextList;
	}

	// WAF: capture specific attribute from the list:
	public List<String> getElementsAttributeList(By locator, String attrName) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleAttrList = new ArrayList<String>();// pc = 0
		for (WebElement e : eleList) {
			String attrValue = e.getAttribute(attrName);
			eleAttrList.add(attrValue);
		}

		return eleAttrList;

	}

	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public void Search(By searchField, By suggestions, String searchKey, String suggName) throws InterruptedException {

		dosendKeys(searchField, searchKey);
		Thread.sleep(3000);
		List<WebElement> suggList = getElements(suggestions);
		System.out.println(suggList.size());

		for (WebElement e : suggList) {
			String text = e.getText();
			System.out.println(text);
			if (text.contains(suggName))
				e.click();
			break;
		}
	}

	public void clickOnElement(By locator, String eleText) {
		List<WebElement> eleList = getElements(locator);
		System.out.println(eleList.size());

		for (WebElement e : eleList) {
			String text = e.getText();
			System.out.println(text);

			if (text.contains(eleText)) {
				e.click();
				break;
			}
		}

	}

	// ***************Select drop Down Utils***************//

	public Select createSelect(By locator) {
		Select select = new Select(getElement(locator));
		return select;
	}

	public void doSelectDropDownByIndex(By locator, int index) {
//		Select select = new Select(getElement(locator));
//		select.selectByIndex(index);
		createSelect(locator).selectByIndex(index);
	}

	public void doSelectDropDownByVisibleText(By locator, String visibleText) {
//		Select select = new Select(getElement(locator));
//		select.selectByVisibleText(visibleText);
		createSelect(locator).selectByVisibleText(visibleText);
	}

	public void doSelectDropDownByValue(By locator, String value) {
//		Select select = new Select(getElement(locator));
//		select.selectByVisibleText(value);
		createSelect(locator).selectByValue(value);
	}

	public int getDropDownOptionsCount(By locator) {
//		Select select = new Select(getElement(locator));
//		int count = select.getOptions().size();
		return createSelect(locator).getOptions().size();
	}

	public void selectDropdownOption(By locator, String dropDownValue) {
//		Select select = new Select(getElement(locator));

		List<WebElement> optionsList = createSelect(locator).getOptions();
		for (WebElement e : optionsList) {

			System.out.println(optionsList.size());

			String text = e.getText();
			if (text.contains(dropDownValue)) {
				e.click();
				break;
			}
		}
	}

	public List<String> getDropdownOptions(By locator) {
		Select select = new Select(getElement(locator));
		List<String> optionsTextList = new ArrayList<String>();
		List<WebElement> optionsList = select.getOptions();
		for (WebElement e : optionsList) {
			String text = e.getText();
			optionsTextList.add(text);
		}

		return optionsTextList;
	}

	public void selectDropDownValue(By locator, String Value) {
		List<WebElement> optionsList = getElements(locator);

		for (WebElement e : optionsList) {
			String text = e.getText();
			if (text.contains(Value)) {
				e.click();
				break;
			}
		}
	}

	public boolean isDropDownMultiple(By locator) {
//		Select select = new Select(getElement(locator));
//		return select.isMultiple() ? true : false;
		return createSelect(locator).isMultiple() ? true : false;
	}

	/**
	 * This method is used to select the values from the drop down. It can select;
	 * 1. single selection 2. Multiple selection 3. All Selection: please pass "all"
	 * as a value to select all the values
	 * 
	 * @param locator
	 * @param values
	 */

	public void selectDropDownMultipleValues(By locator, By optionLocator, String... values) {
		// Select select = new Select(getElement(locator));
		createSelect(locator);

		if (isDropDownMultiple(locator)) {

			if (values[0].equalsIgnoreCase("all")) {
				// List<WebElement> optionsList = driver.findElements(optionLocator);
				List<WebElement> optionsList = getElements(optionLocator);
				for (WebElement e : optionsList) {
					e.click();
				}
			} else {
				for (String value : values) {
					createSelect(locator).selectByVisibleText(value);
				}
			}
		}
	}

	// *****************Actions utils ***************//

	public void doActionsSendkeys(By locator, String value) {
		Actions act = new Actions(driver);
		act.sendKeys(getElement(locator), value).perform();
	}

	public void doActionsClick(By locator) {
		Actions act = new Actions(driver);
		act.click(getElement(locator)).perform();
	}

	public void twoLevelMenu(By parentMenuLocator, By childMenuLocator) throws InterruptedException {
		Actions act = new Actions(driver);
		act.moveToElement(getElement(parentMenuLocator)).build().perform();
		Thread.sleep(1000);
		doClick(childMenuLocator);
	}

	public void fourLevelMenuHandle(By parentMenuLocator, By firstChildMenuLocaor, By secondChildMenuLocaor,
			By thirdChildMenuLocaor) throws InterruptedException {

		Actions act = new Actions(driver);

		doClick(parentMenuLocator);

		Thread.sleep(1000);

		act.moveToElement(driver.findElement(firstChildMenuLocaor)).build().perform();

		Thread.sleep(1000);

		act.moveToElement(driver.findElement(secondChildMenuLocaor)).build().perform();

		Thread.sleep(1000);

		doClick(thirdChildMenuLocaor);

	}

	public void doSendKeysWithPause(By locator, String value) {
		Actions act = new Actions(driver);

		char[] val = value.toCharArray();

		for (char c : val) {
			act.sendKeys(getElement(locator), String.valueOf(c)).pause(500).build().perform();
		}
	}
	
	//***************Wait Utils***************************//
	
	/**
	 * An expectation for checking that an element is present on the DOM of a page.
	 * This does not necessarily mean that the element is visible.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement waitForPresenceOfElement(By loator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(loator));
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible. Visibility means that the element is not only displayed but also
	 * has a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	@Step("waiting for element: {0} and timeout {1}")
	public WebElement waitForVisibilityOfElement(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

	}
	
	/**
	 * An expectation for checking that all elements present on the web page that
	 * match the locator are visible. Visibility means that the elements are not
	 * only displayed but also have a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForVisibilityOfElements(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

	}

	
	/**
	 * An expectation for checking that there is at least one element present on a web page.
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForPresenceOfElements(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
		
	}
	
	public void doClickWithWait(By locator, int timeOut) {
		waitForVisibilityOfElement(locator, timeOut).click();
	}
	
	public void doSendkeysWithWait(By locator,String value, int timeOut) {
		waitForVisibilityOfElement(locator, timeOut).sendKeys("test@gmail.com");
	}
	
	public String waitForTitleContains(String titleFraction, int timeOut) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.titleContains(titleFraction))) {
				return driver.getTitle();
			}
		} catch (TimeoutException e) {
			System.out.println(titleFraction + " title value is not present....");
			e.printStackTrace();
		}
		return null;
	}
	
	@Step("waiting for page title : {0} and timeOut : {1}")
	public String waitForTitleIs(String title, int timeOut) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.titleIs(title))) {
				return driver.getTitle();
			}
		} catch (TimeoutException e) {
			System.out.println(title + " title value is not present....");
			e.printStackTrace();
		}
		return null;
	}
	
	@Step("waiting for url : {0} and timeOut {1}")
	public String waitForURLContains(String urlFraction, int timeOut) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.urlContains(urlFraction))) {
				return driver.getCurrentUrl();
			}
		} catch (TimeoutException e) {
			System.out.println(urlFraction + " url value is not present....");
			e.printStackTrace();
		}
		return null;
	}
	
	public String waitForURLToBe(String url, int timeOut) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.urlToBe(url))) {
				return driver.getCurrentUrl();
			}
		} catch (TimeoutException e) {
			System.out.println(url + " url value is not present....");
			e.printStackTrace();
		}
		return null;
	}
	
	public Alert waitForJSAlert(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.alertIsPresent());
		
	}
	
	public void acceptJSAlert(int timeOut) {
		waitForJSAlert(timeOut).accept();
	}
	
	public void dismissJSAlert(int timeOut) {
		waitForJSAlert(timeOut).dismiss();
	}
	
	public String getJSAlertText(int timeOut) {
		return waitForJSAlert(timeOut).getText();
	}
	
	public void enterValueOnJSAlert(int timeOut,String value) {
		 waitForJSAlert(timeOut).sendKeys(value);
	}
	
	public void waitForFrameByLocator(By frameLocator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

	public void waitForFrameByIndex(int frameIndex, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}

	public void waitForFrameByIdOrName(String IdOrName, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IdOrName));
	}

	public void waitForFrameByElement(WebElement frameElement, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}
	
	public boolean checkNewWindowExist(int timeOut, int expectedNumberOfWindows) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		
		try {
			if (wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows))) {
				return true;
			}
		} catch (TimeoutException e) {
			System.out.println("number of windows are not same....");
		}
		return false;
	}
	
	public WebElement waitForElementWithFluentWait(By locator, int timeOut, int intervalTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(intervalTime))
				.withMessage("--time out is done...element is not found....")
				.ignoring(NoSuchElementException.class)
				.ignoring(ElementNotInteractableException.class);


		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public void waitForFrameWithFluentWait(String frameIDOrName, int timeOut, int intervalTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(intervalTime))
				.withMessage("--time out is done...frame is not found....")
				.ignoring(NoSuchFrameException.class)
				.ignoring(ElementNotInteractableException.class);


		 wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIDOrName));
	}
	
	public Alert waitForJSAlertWithFluentWait(int timeOut, int intervalTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(intervalTime))
				.withMessage("--time out is done...alert is not appeared....")
				.ignoring(NoAlertPresentException.class)
				.ignoring(ElementNotInteractableException.class);


		return wait.until(ExpectedConditions.alertIsPresent());
	}
	
	public WebElement retryingElement(By locator, int timeOut) {

		WebElement element = null;
		int attempts = 0;

		while (attempts < timeOut) {
			try {
				element = getElement(locator);
				System.out.println("element is found...." + locator + " in attempt " + attempts);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found...." + locator + " in attempt " + attempts);
				try {
					Thread.sleep(500);// default polling time = 500 ms
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}

			attempts++;
		}

		if (element == null) {
			System.out.println("element is not found....tried for " + timeOut + " times " + " with the interval of "
					+ 500 + " milli seconds ");
			throw new FrameworkException("No Such Element");
		}

		return element;
	}

	public WebElement retryingElement(By locator, int timeOut, int intervalTime) {

		WebElement element = null;
		int attempts = 0;

		while (attempts < timeOut) {
			try {
				element = getElement(locator);
				System.out.println("element is found...." + locator + " in attempt " + attempts);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found...." + locator + " in attempt " + attempts);
				try {
					Thread.sleep(intervalTime);// custom polling time
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}

			attempts++;
		}

		if (element == null) {
			System.out.println("element is not found....tried for " + timeOut + " times " + " with the interval of "
					+ 500 + " milli seconds ");
			throw new FrameworkException("No Such Element");
		}

		return element;
	}

	
	public boolean isPageloaded(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		String flag = wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'")).toString();
		return Boolean.parseBoolean(flag);
	}

	
}
