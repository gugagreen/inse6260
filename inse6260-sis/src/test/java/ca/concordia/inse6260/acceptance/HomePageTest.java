package ca.concordia.inse6260.acceptance;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.concordia.inse6260.Inse6260SisApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Inse6260SisApplication.class)
@WebIntegrationTest(value = "server.port=8080")
@SeleniumTest
public class HomePageTest {

	private WebDriver browser;

	@Before
	public void setup() {
		browser = new FirefoxDriver();
		browser.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);	
	}
	
	@Test
	public void adminShouldBeAbleToAddRemoveFromCart() {
		// go to home page
		browser.get("http://localhost:8080/");
		// should redirect to login page
		Assert.assertEquals("Login Page", browser.getTitle());
		// login as admin
		login("admin", "1234");
		// should redirect to home page
		Assert.assertEquals("SIS", browser.getTitle());
		// now should be able to go to cart page
		goToCart();
		Assert.assertEquals("Cart", browser.getTitle());
		
		final String courseName = "SOEN691";
		// before select term, course should not be there
		WebElement courseLine = findCourse(courseName);
		Assert.assertNull(courseLine);
		
		// select term
		selectTerm("FALL2016");
		// check if SOEN691 shows up after term select
		courseLine = findCourse(courseName);
		Assert.assertNotNull(courseLine);
		
		// check student does not have course in cart yet
		WebElement cartEntry = findCartEntry(courseName);
		Assert.assertNull(cartEntry);
		
		// select student
		final String studentId = "student1";
		selectStudent(studentId);
		
		// add course to student cart
		WebElement addButton = findAddButton(courseName);
		addButton.click();
		// check if course was added
		cartEntry = findCartEntry(courseName);
		Assert.assertNotNull(cartEntry);
		
		// remove course from student cart
		WebElement deleteButton = findDeleteButton(courseName);
		deleteButton.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// check if course was removed
		cartEntry = findCartEntry(courseName);
		Assert.assertNull(cartEntry);
		
		browser.quit();
	}
	
	private void goToCart() {
		browser.findElement(By.linkText("Cart")).click();
	}
	
	private void login(final String username, final String password) {
		WebElement username_txt = browser.findElement(By.name("username"));
		username_txt.sendKeys(username);
		WebElement pw_txt = browser.findElement(By.name("password"));
		pw_txt.sendKeys(password);
		browser.findElement(By.cssSelector("input[type='submit']")).click();
	}
	
	private void selectTerm(final String term) {
		Select term_select = new Select(browser.findElement(By.id("term_select")));
		term_select.selectByVisibleText(term);
	}
	
	private void selectStudent(final String studentId) {
		Select term_select = new Select(browser.findElement(By.id("student_select")));
		term_select.selectByVisibleText(studentId);
	}
	
	private WebElement findCourse(final String courseName) {
		String courseXPath = "//div[@id='allCourses']/table/tbody/tr[td//text()[contains(., '" + courseName + "')]]";
		return findByXPath(courseXPath);
	}
	
	private WebElement findAddButton(final String courseName) {
		String buttonXPath = "//div[@id='allCourses']/table/tbody/tr/td[normalize-space() ='" + courseName + "']/../td[7]";
		return findByXPath(buttonXPath);
	}
	
	private WebElement findCartEntry(final String courseName) {
		String courseXPath = "//div[@id='cart']/table/tbody/tr[td//text()[contains(., '" + courseName + "')]]";
		return findByXPath(courseXPath);
	}
	
	private WebElement findDeleteButton(final String courseName) {
		String buttonXPath = "//div[@id='cart']/table/tbody/tr/td[normalize-space() ='" + courseName + "']/../td[5]";
		return findByXPath(buttonXPath);
	}
	
	private WebElement findByXPath(final String xPath) {
		WebElement element = null;
		List<WebElement> list = browser.findElements(By.xpath(xPath));
		if (list.size() > 0) {
			element = list.get(0);
		}
		return element;
	}
}
