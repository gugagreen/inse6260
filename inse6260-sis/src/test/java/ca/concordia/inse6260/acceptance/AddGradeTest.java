package ca.concordia.inse6260.acceptance;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class AddGradeTest extends AbstractSisAcceptanceTest {

	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@Test
  public void testAddGrade() throws Exception {
		// go to home page
		gotoHomePage();
		
		// login then logout
		login("prof1", "1234");
		// should redirect to home page
		Assert.assertEquals("SIS", driver.getTitle());
		driver.findElement(By.linkText("Grades")).click();
	    new Select(driver.findElement(By.id("term_select"))).selectByVisibleText("SUMMER2016");
	    driver.findElement(By.id("btn_open_1")).click();
	    driver.findElement(By.id("btn_update_grades_1")).click();
	    //assert that the success header popped up after inserting grade
	    assertTrue(isElementPresent(By.cssSelector("div.modal-header")));
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
	}