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

public class LogoutTest extends AbstractSisAcceptanceTest {

	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@Test
	public void testLogout() throws Exception {
		// go to home page
		gotoHomePage();
		
		// login then logout
		login("admin", "1234");
		// should redirect to home page
		Assert.assertEquals("SIS", driver.getTitle());
		logout();
		// should redirect to login page
		Assert.assertEquals("Login Page", driver.getTitle());
		
		// login, go to Cart page, then logout
		login("admin", "1234");
		driver.findElement(By.linkText("Cart")).click();
		Assert.assertEquals("Cart", driver.getTitle());
		logout();
		// should redirect to login page
		Assert.assertEquals("Login Page", driver.getTitle());
		
		// login, go to Transcript page, then logout
		login("admin", "1234");
		driver.findElement(By.linkText("Transcript")).click();
		Assert.assertEquals("Transcript", driver.getTitle());
		logout();
		// should redirect to login page
		Assert.assertEquals("Login Page", driver.getTitle());
		
		// login, go to Payment page, then logout
		login("admin", "1234");
		driver.findElement(By.linkText("Payment")).click();
		Assert.assertEquals("Payment", driver.getTitle());
		logout();
		// should redirect to login page
		Assert.assertEquals("Login Page", driver.getTitle());
		
		// login, go to Grades page, then logout
		login("admin", "1234");
		driver.findElement(By.linkText("Grades")).click();
		Assert.assertEquals("Grades", driver.getTitle());
		logout();
		// should redirect to login page
		Assert.assertEquals("Login Page", driver.getTitle());
		
		// login, go to Change Password page, then logout
		login("admin", "1234");
		driver.findElement(By.linkText("Change Password")).click();
		Assert.assertEquals("Change Password", driver.getTitle());
		logout();
		// should redirect to login page
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

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}