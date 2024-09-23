package derivResponsiveTesting;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import net.bytebuddy.build.Plugin.Factory.UsingReflection.Priority;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ResponsiveTest {
	static ExtentTest graphicalTest;
	static ExtentReports graphicalReport;
	@BeforeSuite
	public void beforeSuiteStart() {
		graphicalReport = new ExtentReports();
		graphicalReport.attachReporter(new com.aventstack.extentreports.reporter.ExtentSparkReporter("C:\\\\Users\\\\ENVY\\\\OneDrive\\\\Desktop\\\\Material\\\\Automation_Testing\\\\Deriv\\\\results.html"));
//		graphicalReport = new ExtentReports("C:\\Users\\ENVY\\OneDrive\\Desktop\\Material\\Automation_Testing\\Deriv\\results.html",true);
		
	}
	
	@BeforeMethod
	public void beforeEachTest(Method testMethod) {
		graphicalTest = graphicalReport.createTest(testMethod.getName());
	}
	@DataProvider
	public Object[][] mobileEmulations(){
		return new Object[][] {
			{"iPhone SE"},
			{"iPhone 14 Pro Max"},
			{"iPhone 12 Pro"},
			{"Pixel 7"},
			{"iPad Mini"},
			{"Asus Zenbook Fold"},
			{"Samsung Galaxy A51/71"},
			{"Samsung Galaxy S8+"}
		};
	}
	@Test (priority=0,dataProvider = "mobileEmulations")
	public void validateResponsive(String emulation) throws InterruptedException {
		Map<String,String> deviceMobEmu = new HashMap<>();
		deviceMobEmu.put("deviceName",emulation);
		ChromeOptions chromeOpt = new ChromeOptions();
		chromeOpt.setExperimentalOption("mobileEmulation", deviceMobEmu);
		WebDriver driver = new ChromeDriver(chromeOpt);
		try {
		driver.get("https://derivfe.github.io/qa-test/settings");
		
		//Validating the page title 
		String actualtitle = driver.getTitle();
		String expectedtitle = "BinaryOptions";
		System.out.println("Page title:"+actualtitle +"Responsive-" + emulation);
		if (actualtitle.equals(expectedtitle)) {
			graphicalTest.log(Status.PASS, "Title Validation Passed for "+ emulation);
		}else {
			graphicalTest.log(Status.FAIL, "Title Validation Failed for " +emulation);
		}
		
		//Validating the form 
		WebElement salutionDropDown = driver.findElement(By.xpath("//select[@id='salutation']"));
		Select selectsalutation = new Select(salutionDropDown);
		selectsalutation.selectByVisibleText("Mr");
		Thread.sleep(2000);
		driver.findElement(By.id("fname")).sendKeys("Alexa");
		driver.findElement(By.id("lname")).sendKeys("Crescent");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='dob']")).sendKeys("22-04-1999");
		driver.findElement(By.xpath("//textarea[@id='address']")).sendKeys("XXXX QA TESTING XXXXXX");
		driver.findElement(By.xpath("//input[@id='city']")).sendKeys("TEST CITY");
		driver.findElement(By.xpath("//input[@id='state']")).sendKeys("TEST STATE");
		driver.findElement(By.xpath("//input[@id='zip']")).sendKeys("5435265");
		driver.findElement(By.xpath("//input[@id='tel']")).sendKeys("2434-556-6753");
		driver.findElement(By.xpath("//input[@id='email']")).sendKeys("alexacrecsent@test.com");
		driver.findElement(By.xpath("//button[@id='btn-submit-profile']")).click();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement successmsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"profile-form-msg\"]")));
		System.out.println(successmsg);
		String message = successmsg.getText();
		if(message.equals("Your profile has been updated.")) {
			graphicalTest.log(Status.PASS, "Form Validation passed for "+ emulation);
		}else {
			graphicalTest.log(Status.FAIL, "Form Validation failed for " +emulation);
		}
		String screenshotPath = screenCapture(driver);
		graphicalTest.log(Status.PASS,"Test Done With"+emulation).addScreenCaptureFromPath(screenshotPath);
		System.out.println("Test Done with " +emulation);
		}catch(IOException e) {
		graphicalTest.log(Status.FAIL,"Failed Test for"+emulation);
		System.out.println("Test Done with "+emulation);
	}
		driver.quit();
	}
	
	@AfterMethod
	public void afterEachTest() {
		graphicalReport.flush();
	}
	
	
	public static String screenCapture(WebDriver driver) throws IOException {
		TakesScreenshot takeSS = (TakesScreenshot) driver;
		
		File sourceOutputFile = takeSS.getScreenshotAs(OutputType.FILE);
		File Dest = new File("C:\\Users\\ENVY\\OneDrive\\Desktop\\Material\\Automation_Testing\\Deriv\\Screenshot\\FullPage.png");
		FileUtils.copyFile(sourceOutputFile,Dest);
		String errflpath = Dest.getAbsolutePath();
		return errflpath;
	}
	
}