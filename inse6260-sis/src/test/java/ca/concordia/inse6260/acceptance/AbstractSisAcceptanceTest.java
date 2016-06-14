package ca.concordia.inse6260.acceptance;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.concordia.inse6260.Inse6260SisApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Inse6260SisApplication.class)
@WebIntegrationTest(value = "server.port=8080")
@SeleniumTest
public abstract class AbstractSisAcceptanceTest {
	
	public static final String BASE_URL = "http://localhost:8080/";

	protected WebDriver driver;
	
	/**
	 * Should be called by child classes on setup. Sets driver and timeout. 
	 */
	public void setup() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);	
	}
	
	public void gotoHomePage() {
		driver.get(BASE_URL);
	}
	
	protected void login(final String username, final String password) {
		WebElement username_txt = driver.findElement(By.name("username"));
		username_txt.clear();
		username_txt.sendKeys(username);
		WebElement pw_txt = driver.findElement(By.name("password"));
		pw_txt.clear();
		pw_txt.sendKeys(password);
		driver.findElement(By.cssSelector("input.btn.btn-default")).click();
	}
	
	protected void logout() {
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
	}
	
	/**
	 * find an element on page by xpath.
	 * @param xPath
	 * @return the first matching WebElement, or null if none was found.
	 */
	public WebElement findByXPath(final String xPath) {
		WebElement element = null;
		List<WebElement> list = driver.findElements(By.xpath(xPath));
		if (list.size() > 0) {
			element = list.get(0);
		}
		return element;
	}
}
