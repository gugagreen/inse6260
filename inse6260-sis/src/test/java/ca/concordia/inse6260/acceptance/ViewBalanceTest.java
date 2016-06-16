package ca.concordia.inse6260.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;

public class ViewBalanceTest extends AbstractSisAcceptanceTest {

	private StringBuffer verificationErrors = new StringBuffer();


	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@Test
  public void testViewBalance() throws Exception {
		// go to home page
		gotoHomePage();
		
		// login then logout
		login("student1", "1234");
		// should redirect to home page
		Assert.assertEquals("SIS", driver.getTitle());
    driver.findElement(By.linkText("Payment")).click();
    assertEquals("Account Balance Viewer", driver.findElement(By.cssSelector("div.row")).getText());
    assertEquals("Payment", driver.getTitle());
    assertEquals("Debts", driver.findElement(By.cssSelector("h4")).getText());
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
