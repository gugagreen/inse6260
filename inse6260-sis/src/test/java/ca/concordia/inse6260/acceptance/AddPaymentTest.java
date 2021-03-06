package ca.concordia.inse6260.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class AddPaymentTest extends AbstractSisAcceptanceTest {

	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@Test
	public void testAddPayment() throws Exception {
		// go to home page
		gotoHomePage();

		// login then logout
		login("admin", "123456");
		// should redirect to home page
		Assert.assertEquals("SIS", driver.getTitle());
		driver.findElement(By.linkText("Payment")).click();
		new Select(driver.findElement(By.id("student_select"))).selectByVisibleText("student1 - QUEBEC");
		driver.findElement(By.cssSelector("option[value=\"student1\"]")).click();
		BigDecimal initialTotalBalance = findTotalBalance();
		
		driver.findElement(By.id("paymentValue")).clear();
		driver.findElement(By.id("paymentValue")).sendKeys("0.1");
		driver.findElement(By.cssSelector("button.btn.btn-default")).click();
		driver.findElement(By.cssSelector("button.close")).click();
		BigDecimal finalTotalBalance = findTotalBalance();
		assertEquals(initialTotalBalance.add(new BigDecimal("0.1")), finalTotalBalance);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	private BigDecimal findTotalBalance() {
		String totalBalanceText = driver.findElement(By.xpath("//div[@id='balance']/h3[1]")).getText();
		String totalBalanceValue = totalBalanceText.replace("Total balance: ", "");
		return new BigDecimal(totalBalanceValue);
	}
}
