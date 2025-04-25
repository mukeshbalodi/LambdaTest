package Base;

import com.aventstack.extentreports.*;
import Utils.ExtentManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class BaseClassLocal {

    
    private static ExtentReports extent;

    // Thread-safe WebDriver and ExtentTest
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    public ExtentTest getTest() {
        return test.get();
    }

    @BeforeSuite(alwaysRun = true)
    public synchronized void setupReport() {
        if (extent == null) {
            extent = ExtentManager.getInstance();
        }
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "browserVersion", "platform"})
    public void setup(@Optional("chrome") String browser,
                      @Optional("latest") String browserVersion,
                      @Optional("local") String platform,
                      Method method) {

        System.out.println("=== Starting setup for: " + browser + " ===");

        ExtentTest extentTest = extent.createTest("Test: " + method.getName())
                .assignCategory(browser)
                .assignDevice(browser + " " + browserVersion + " on " + platform);

        test.set(extentTest);

        extentTest.log(Status.INFO, "Starting Test: " + method.getName());
        extentTest.log(Status.INFO, "Browser: " + browser);
        extentTest.log(Status.INFO, "Browser Version: " + browserVersion);
        extentTest.log(Status.INFO, "Platform: " + platform);

        try {
            WebDriver localDriver;

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    localDriver = new ChromeDriver();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    localDriver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    localDriver = new EdgeDriver();
                    break;
                case "safari":
                    localDriver = new SafariDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid browser: " + browser);
            }

            localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            localDriver.manage().window().maximize();

            driver.set(localDriver);

        } catch (Exception e) {
            extentTest.fail("Browser setup failed for: " + browser + "<br>" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Browser setup failed for: " + browser, e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void teardown(ITestResult result) {
        ExtentTest extentTest = test.get();
        WebDriver localDriver = driver.get();

        if (extentTest != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshotPath = takeScreenshot(result.getName());
                String relativePath = "./screenshots/" + result.getName() + ".png";

                if (screenshotPath != null) {
                    extentTest.fail("Test Failed: " + result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
                } else {
                    extentTest.fail("Test Failed: " + result.getThrowable());
                }

            } else if (result.getStatus() == ITestResult.SUCCESS) {
                extentTest.pass("Test Passed");
            } else if (result.getStatus() == ITestResult.SKIP) {
                extentTest.skip("Test Skipped");
            }

            extentTest.log(Status.INFO, "Closing browser");
        }

        if (localDriver != null) {
            localDriver.quit();
            System.out.println("Closed browser: " + localDriver);
            driver.remove();
        }

        test.remove();
    }

    @AfterSuite(alwaysRun = true)
    public void generateReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public String takeScreenshot(String testName) {
        WebDriver localDriver = driver.get();
        try {
            File src = ((TakesScreenshot) localDriver).getScreenshotAs(OutputType.FILE);
            String path = System.getProperty("user.dir") + "/test-output/screenshots/" + testName + ".png";
            File destination = new File(path);
            Files.createDirectories(destination.getParentFile().toPath());
            Files.copy(src.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return path;
        } catch (IOException | WebDriverException e) {
            e.printStackTrace();
            return null;
        }
    }
}
