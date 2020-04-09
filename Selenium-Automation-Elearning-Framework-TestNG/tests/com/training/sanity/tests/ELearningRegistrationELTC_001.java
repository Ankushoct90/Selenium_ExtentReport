package com.training.sanity.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.training.generics.BaseTest;
import com.training.generics.ExtentManager;
import com.training.generics.ScreenShot;
import com.training.pom.LoginPOM;
import com.training.pom.RegistrationPOM;
import com.training.utility.DriverFactory;
import com.training.utility.DriverNames;

public class ELearningRegistrationELTC_001 extends BaseTest{
	/*
	 * public WebDriver dri; public ELearningRegistrationELTC_001(WebDriver dri) {
	 * super(dri); }
	 */

	private WebDriver driver;

	private String baseUrl;
	private static Properties properties;
	private RegistrationPOM registrationPOM;
	private LoginPOM loginPOM;
	private ScreenShot screenShot;
	public static String fName;
	public static String lName;

	@BeforeClass
	public void setUpBeforeClass() throws IOException {
		properties = new Properties();
		FileInputStream inStream = new FileInputStream("./resources/others.properties");
		properties.load(inStream);
	}

	@BeforeMethod
	public void setUp() {
		driver = DriverFactory.getDriver(DriverNames.CHROME);
		registrationPOM = new RegistrationPOM(driver,test);
		loginPOM = new LoginPOM(driver,test);
		baseUrl = properties.getProperty("baseURL");
		fName = properties.getProperty("Fname");
		lName = properties.getProperty("Lname");
		
		// initiateReport(testcaseName, 1);*/

		screenShot = new ScreenShot(driver);
		driver.get(baseUrl);
		driver.manage().window().maximize();
	}

	@Test
	public void validRegistrationTest() {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		loginPOM.clickSignUp();
		System.out.println("firstName is : " + fName);
		System.out.println("Last name is : " + lName);
		registrationPOM.enterFirstName("reva");
		registrationPOM.enterLastName("sharma");
		registrationPOM.enterEmail("revasharma@gmail.com");
		registrationPOM.enterUserName("reva10985678");
		registrationPOM.enterPassword("reva123");
		registrationPOM.enterPhone("9876455463");
		registrationPOM.checkAndSelectRadioButton(registrationPOM.getStudent());
		registrationPOM.clickRegisterBtn();
		// screenShot.captureScreenShot("RegistrationSuccessful");

		String registrationMessageFromApp = driver.findElement(By.xpath("//div[@class='col-xs-12 col-md-12']"))
				.getText();

		System.out.println("Actual Message is :\n" + registrationMessageFromApp);

		System.out.println("******************");

		String successRegistrationMessage = "Dear " + fName + " " + lName + "," + "\n" + "\n"
				+ "Your personal settings have been registered.\n"
				+ "An e-mail has been sent to remind you of your login and password.\n"
				+ "You can now select, in the list, the course you want access to.";

		System.out.println("Expected Message is :\n" + successRegistrationMessage);

		Assert.assertEquals(registrationMessageFromApp, successRegistrationMessage);

	}

	@AfterMethod
	public void afterMethod() throws InterruptedException {
		loginPOM.clickUserDropdown();
		loginPOM.clickLogoff();
		Thread.sleep(3000);
		driver.close();
	}

}
