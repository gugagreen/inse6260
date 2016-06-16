package ca.concordia.inse6260.acceptance;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginTest extends AbstractSisAcceptanceTest{
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@Test
	public void testLogin() throws Exception {
		// go to home page
		gotoHomePage();
		
		// login then logout
		login("admin", "1234");
		// should redirect to home page
		Assert.assertEquals("SIS", driver.getTitle());
		logout();
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
