package ca.concordia.inse6260.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;


public class ViewCartAdminTest extends AbstractSisAcceptanceTest {

	private StringBuffer verificationErrors = new StringBuffer();
	
	@Before
	public void setup() {
		super.setup();
	}

  @Test
  public void testViewCartAdmin() throws Exception {
	// go to home page
			gotoHomePage();
			
			// login then logout
			login("admin", "1234");
			// should redirect to home page
			Assert.assertEquals("SIS", driver.getTitle());

			driver.findElement(By.linkText("Cart")).click();
			
			new Select(driver.findElement(By.id("term_select"))).selectByVisibleText("SUMMER2016");
			driver.findElement(By.cssSelector("option[value=\"SUMMER2016\"]")).click();
			new Select(driver.findElement(By.id("student_select"))).selectByVisibleText("student1");
			driver.findElement(By.cssSelector("option[value=\"student1\"]")).click();
			assertEquals("INSE6260", driver.findElement(By.xpath("//div[@id='cart']/table/tbody/tr[2]/td[2]")).getText());
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
