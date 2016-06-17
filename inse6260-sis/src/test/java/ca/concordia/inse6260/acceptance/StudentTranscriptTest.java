package ca.concordia.inse6260.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;


public class StudentTranscriptTest extends AbstractSisAcceptanceTest {

	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		super.setup();
	}

	@Test
  public void testStudentViewTranscript() throws Exception {
		// go to home page
				gotoHomePage();
				
				// login then logout
				login("student1", "123456");
				// should redirect to home page
				Assert.assertEquals("SIS", driver.getTitle());
    assertTrue(isElementPresent(By.linkText("Transcript")));
    driver.findElement(By.linkText("Transcript")).click();
    assertEquals("INSE6260", driver.findElement(By.xpath("//div[@id='transcript']/table/tbody/tr[2]/td[2]")).getText());
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
