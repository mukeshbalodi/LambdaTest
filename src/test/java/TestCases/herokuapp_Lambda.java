package TestCases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import Base.BaseClassLambda;
import PageObjects.AlertPage;
import PageObjects.LoginPage;
import Utils.ConfigReader;

public class herokuapp_Lambda extends BaseClassLambda{
	LoginPage page; 
	AlertPage alpage;
	ConfigReader config; 
	


	    @Test(priority = 1)
	    public void ValidLogin() {
	        LoginPage page = new LoginPage(driver);
	        config = new ConfigReader();

	        driver.get("https://the-internet.herokuapp.com/login");
	        driver.manage().window().maximize();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	        page.enterUsername(config.getAppValidUsername());
	        page.enterPassword(config.getAppValidPassword());
	        page.clickLogin();

	        WebElement flashMessage = driver.findElement(By.id("flash"));
	        assertTrue(flashMessage.isDisplayed(), "Login failed: Success message not displayed.");
	    
	}
	    
	 

	        @Test(priority = 2)
	        public void inValidLogin() {
	            page = new LoginPage(driver);
	            config = new ConfigReader();

	            driver.get("https://the-internet.herokuapp.com/login");
	            driver.manage().window().maximize();
	            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	            page.enterUsername(config.getAppInvalidUsername());
	            page.enterPassword(config.getAppInvalidPassword());
	            page.clickLogin();

	            WebElement logoutButton = driver.findElement(By.xpath("/html[1]/body[1]/div[2]/div[1]/div[1]/a[1]/i[1]"));
		        assertTrue(logoutButton.isDisplayed(), "Login failed: Success message not displayed.");
	        
	    }


	        @Test(priority = 3)
	        public void JsAlert() {
	        	alpage = new AlertPage(driver);
	        	config = new ConfigReader();
	        	driver.get("https://the-internet.herokuapp.com/javascript_alerts");
	        	driver.manage().window().maximize();
	        	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	        	alpage.clickJsAlert();
	        	alpage.acceptAlert();
	        	String ele= driver.findElement(By.id("result")).getText();
	        	assertEquals(ele, "You successfully clicked an alert");
	        	alpage.clickJsConfirm();
	        	alpage.acceptAlert();
	        	String ele1= driver.findElement(By.id("result")).getText();
	        	assertEquals(ele1, "You clicked: Ok");
	        	alpage.clickJsPrompt();
	        	alpage.enterTextInPrompt("Mukesh Chandra Balodi");
	        	String ele2 = driver.findElement(By.id("result")).getText();
	        	assertEquals(ele2, "You entered: Mukesh Chandra Balodi");

	        }
}
