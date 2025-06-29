package TestCases;

import static org.testng.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import Base.BaseClassLocal;
import PageObjects.AlertPage;
import PageObjects.LoginPage;
import Utils.CSVReaderUtil;
import Utils.ConfigReader;


public class herokuapp_Local extends BaseClassLocal {

    LoginPage page; 
    ConfigReader config; 
    AlertPage alpage;
  //  public static String testCaseID = "";
    @Test(priority = 1, dataProvider = "loginData", dataProviderClass = CSVReaderUtil.class)
    public void TestLogin(String username, String password) {
       //  testCaseID = "C18";  Use only when not taking data from csv and seperate test case ID in testrail.
        page = new LoginPage(getDriver());
        getDriver().get("https:the-internet.herokuapp.com/login");
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        page.enterUsername(username); // from CSV
        getTest().log(Status.INFO, "Username entered in the username field ");
        page.enterPassword(password); // from CSV
        getTest().log(Status.INFO, "Password entered in the password field");
        page.clickLogin();
        getTest().log(Status.INFO, "Clicked on Login Button");

        WebElement ele = getDriver().findElement(By.xpath("//div[@id='flash']"));
        try {
            Assert.assertTrue(ele.isDisplayed());
            getTest().log(Status.PASS, "Element displayed: " + ele.getText());
        } catch (AssertionError e) {
            getTest().log(Status.FAIL, "Element not displayed. Login failed.");
            Assert.fail("Test failed: Login was unsuccessful.");
        }
    }


    @Test(priority = 2)
    public void inValidLogin() {
        page = new LoginPage(getDriver());
        config = new ConfigReader();
        getDriver().get("https:the-internet.herokuapp.com/login");
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        page.enterUsername(config.getAppInvalidUsername());
        getTest().log(Status.INFO, "InValid Username entered in the username field");
        page.enterPassword(config.getAppInvalidPassword());
        getTest().log(Status.INFO, "InValid password entered into the password field");
        page.clickLogin();
        getTest().log(Status.INFO, "Clicked on Login Button");

        WebElement ele = getDriver().findElement(By.xpath("//div[@id='flash']"));
        try {
            Assert.assertTrue(ele.isDisplayed());
            getTest().log(Status.PASS, "Element displayed : " + ele.getText());
        } catch (AssertionError e) {
            getTest().log(Status.FAIL, "Test fail ");
        }
    }

    @Test(priority = 3)
    public void JsAlert() {
        getTest().log(Status.INFO, "Navigating to JavaScript Alerts page");
        try {
            alpage = new AlertPage(getDriver());
            config = new ConfigReader();
            getDriver().get("https:the-internet.herokuapp.com/javascript_alerts");
            getDriver().manage().window().maximize();
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            getTest().log(Status.PASS, "Page loaded and window maximized");
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Failed to load or maximize page: " + e.getMessage());
        }

    //     Handle JS Alert
        try {
            getTest().log(Status.INFO, "Clicking JS Alert");
            alpage.clickJsAlert();
            alpage.acceptAlert();
            String ele = getDriver().findElement(By.id("result")).getText();
            assertEquals(ele, "You successfully clicked an alert");
            getTest().log(Status.PASS, "JS Alert handled successfully and verified message");
           
        } catch (AssertionError ae) {
            getTest().log(Status.FAIL, "Assertion failed for JS Alert: " + ae.getMessage());
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Exception while handling JS Alert: " + e.getMessage());
        }

   //      Handle JS Confirm
        try {
            getTest().log(Status.INFO, "Clicking JS Confirm");
            alpage.clickJsConfirm();
            alpage.acceptAlert();
            String ele1 = getDriver().findElement(By.id("result")).getText();
            assertEquals(ele1, "You clicked: Ok");
            getTest().log(Status.PASS, "JS Confirm handled and verified successfully");
        } catch (AssertionError ae) {
            getTest().log(Status.FAIL, "Assertion failed for JS Confirm: " + ae.getMessage());
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Exception while handling JS Confirm: " + e.getMessage());
        } 

//         Handle JS Prompt
        try {
            getTest().log(Status.INFO, "Clicking JS Prompt and entering text");
            alpage.clickJsPrompt();
            alpage.enterTextInPrompt("Mukesh Chandra Balodi");
            String ele2 = getDriver().findElement(By.id("result")).getText();
            assertEquals(ele2, "You entered: Mukesh Chandra Balodi");
            getTest().log(Status.PASS, "JS Prompt handled successfully with correct input");
        } catch (AssertionError ae) {
            getTest().log(Status.FAIL, "Assertion failed for JS Prompt: " + ae.getMessage());
        } catch (Exception e) {
            getTest().log(Status.FAIL, "Exception while handling JS Prompt: " + e.getMessage());
        }
    }
    
    @Test(priority =4 )
    public void searchGoogle() {
    	getDriver().get("https://www.google.com/");
    	getTest().info("URL Entered");
    	getDriver().findElement(By.name("q")).sendKeys("Mukesh balodi");
    	getTest().info("text entered into search bar");
    	Actions action = new Actions(getDriver());
    	action.keyDown(Keys.ENTER);
    	getTest().info("text entered into search bar");
    String actualTitle=	getDriver().getTitle();
    String expectedTitle = "Mukesh balodi - Google Search";
    if(actualTitle.equals(expectedTitle)) {
    	getTest().pass(" Test pass title Matched");
    }
    else {
    	getTest().fail("Test Failed title did't matched with expected title");
    	}
    }

    }

