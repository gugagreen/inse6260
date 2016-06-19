package ca.concordia.inse6260.acceptance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class AddRemoveCartTest extends AbstractSisAcceptanceTest {

	@Before
	public void setup() {
		super.setup();
	}
	
	@Test
	public void adminShouldBeAbleToAddRemoveFromCart() throws InterruptedException {
		// go to home page
		gotoHomePage();
		// should redirect to login page
		Assert.assertEquals("Login Page", driver.getTitle());
		// login as admin
		login("admin", "123456");
		// should redirect to home page
		Assert.assertEquals("SIS", driver.getTitle());
		// now should be able to go to cart page
		goToCart();
		Assert.assertEquals("Cart", driver.getTitle());
		
		final String courseName = "COMP6961";
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
		
		Thread.sleep(1000);
		
		// remove course from student cart
		WebElement deleteButton = findDeleteButton(courseName);
		deleteButton.click();
		Thread.sleep(1000);
		// check if course was removed
		cartEntry = findCartEntry(courseName);
		Assert.assertNull(cartEntry);
		
		driver.quit();
	}
	
	private void goToCart() {
		driver.findElement(By.linkText("Cart")).click();
	}
	
	private void selectTerm(final String term) {
		Select term_select = new Select(driver.findElement(By.id("term_select")));
		term_select.selectByVisibleText(term);
	}
	
	private void selectStudent(final String studentId) {
		Select term_select = new Select(driver.findElement(By.id("student_select")));
		term_select.selectByVisibleText(studentId);
	}
	
	private WebElement findCourse(final String courseName) {
		String courseXPath = "//div[@id='allCourses']/table/tbody/tr[td//text()[contains(., '" + courseName + "')]]";
		return findByXPath(courseXPath);
	}
	
	private WebElement findAddButton(final String courseName) {
		String buttonXPath = "//div[@id='allCourses']/table/tbody/tr/td[normalize-space() ='" + courseName + "']/../td[11]";
		return findByXPath(buttonXPath);
	}
	
	private WebElement findCartEntry(final String courseName) {
		String courseXPath = "//div[@id='cart']/table/tbody/tr[td//text()[contains(., '" + courseName + "')]]";
		return findByXPath(courseXPath);
	}
	
	private WebElement findDeleteButton(final String courseName) {
		String buttonXPath = "//div[@id='cart']/table/tbody/tr/td[normalize-space() ='" + courseName + "']/../td[9]";
		return findByXPath(buttonXPath);
	}
}
