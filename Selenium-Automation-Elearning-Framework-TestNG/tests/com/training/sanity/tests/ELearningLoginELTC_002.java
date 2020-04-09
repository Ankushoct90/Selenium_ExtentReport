package com.training.sanity.tests;

import org.testng.annotations.Test;

import com.training.generics.BaseTest;
import com.training.generics.ScreenShot;
import com.training.pom.LoginPOM;
import com.training.pom.MyCourcesPOM;
import com.training.pom.RegistrationPOM;
import com.training.utility.DriverFactory;
import com.training.utility.DriverNames;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class ELearningLoginELTC_002 extends BaseTest{

	private WebDriver driver;
	private String baseUrl;
	private static Properties properties;
	private MyCourcesPOM myCourcesPOM;
	private LoginPOM loginPOM;
	private ScreenShot screenShot;

	String expectedMessage;
	String fName;
	String lName;

	@BeforeClass
	public void setUpBeforeClass() throws IOException {
		properties = new Properties();
		FileInputStream inStream = new FileInputStream("./resources/others.properties");
		properties.load(inStream);
	}

	@BeforeMethod
	public void setUp() {
		driver = DriverFactory.getDriver(DriverNames.CHROME);
		loginPOM = new LoginPOM(driver,test);
		myCourcesPOM = new MyCourcesPOM(driver,test);
		baseUrl = properties.getProperty("baseURL");
		fName = properties.getProperty("Fname");
		lName = properties.getProperty("Lname");
		screenShot = new ScreenShot(driver);
		driver.get(baseUrl);
		driver.manage().window().maximize();
	}

	@Test
	public void validLoginTest() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		loginPOM.sendUserName("reva1234");
		loginPOM.sendPassword("reva123");
		loginPOM.clickLoginBtn();
		screenShot.captureScreenShot("LoginSuccessful");
		
		expectedMessage = "Hello "+fName+" "+lName+" and welcome,";
		System.out.println("Expected message is: "+expectedMessage);	
		
		
		String loginMessageFromApp = myCourcesPOM.getLoginMessage();
		System.out.println("Actual message is: "+loginMessageFromApp);

		Assert.assertEquals(true, loginMessageFromApp.contains(expectedMessage));

	}

	@AfterMethod
	public void afterMethod() throws InterruptedException {
		loginPOM.clickUserDropdown();
		loginPOM.clickLogoff();
		Thread.sleep(3000);
		driver.close();
	}

}
