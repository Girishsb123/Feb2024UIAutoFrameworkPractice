package com.qa.opencart.tests;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.pages.SearchResultsPage;

public class AccountsPageTest extends BaseTest {
	
	private static final Logger log = LogManager.getLogger(AccountsPageTest.class);

	@BeforeClass
	public void accSetUp() {
		accPage = loginPage.doLogin(prop.getProperty("username"),prop.getProperty("password"));
	}
	
	@Test
	public void accPageTitleTest() {
		Assert.assertEquals(accPage.getAccPageTitle(), AppConstants.ACCOUNTS_PAGE_TITLE);
	}
	
	@Test
	public void accPageURLTest() {
		Assert.assertTrue(accPage.getAccPageUrl().contains(AppConstants.ACC_PAGE_URL_FRACTION));
	}
	
	@Test
	public void isLogoutLinkExistTest() {
		Assert.assertTrue(accPage.isLogoutLinkExist());
	}
	
	@Test
	public void isSearchFieldExistTest() {
		Assert.assertTrue(accPage.isSearchFieldExist());
	}
	
	@Test
	public void accPageHeadersCountTest() {
		List<String> actAccPageHeadersList = accPage.getAccountsHeaders();
		System.out.println(actAccPageHeadersList);
		Assert.assertEquals(actAccPageHeadersList.size(), AppConstants.ACCOUNTS_PAGE_HEADERS_COUNT);
	}
	
	@Test
	public void accPageHeadersTest() {
		List<String> actAccPageHeadersList = accPage.getAccountsHeaders();
//		System.out.println(actAccPageHeadersList);
		log.info(actAccPageHeadersList);
		//sort the actual
		//sort the expected
		//compare
		Assert.assertEquals(actAccPageHeadersList, AppConstants.ACCOUNTS_PAGE_HEADERS_LIST);
	}
	
	@Test
	public void searchTest() {
		searchResultsPage = accPage.doSearch("MacBook");
		productInfoPage   = searchResultsPage.selectProduct("MacBook Pro");
		String actProductHeader = productInfoPage.getProductHeaderName();
		Assert.assertEquals(actProductHeader, "MacBook Pro");
	}
	

}
