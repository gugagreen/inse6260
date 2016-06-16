package ca.concordia.inse6260.acceptance;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;

public class LoginFailureTest extends AbstractSisAcceptanceTest{
	
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@Test
  public void testLoginFailure() throws Exception {
		
		// go to home page
		gotoHomePage();
		// login then logout
		login("admin", "failtest");
		Assert.assertEquals("Login Page", driver.getTitle());
}

 

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
}
