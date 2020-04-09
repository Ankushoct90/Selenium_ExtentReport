package com.training.sanity.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.training.generics.BaseTest;
import com.training.generics.GenericMethods;
import com.training.pom.LoginPOM;
import com.training.pom.MyCourcesPOM;
import com.training.pom.ProfilePOM;
import com.training.utility.DriverFactory;
import com.training.utility.DriverNames;

public class ELearningChangePasswordELTC_003 extends BaseTest {

	private String baseUrl;
	private static Properties properties;
	private ProfilePOM profilePOM;
	private LoginPOM loginPOM;
	private MyCourcesPOM myCourcesPOM;
	public GenericMethods genericMethods;
	public String expectedMessage;
	public static final char FSC = File.separatorChar;
	public static final String USER_DIR = System.getProperty("user.dir");
	
	
	@Test
	public void testMeth() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",USER_DIR + FSC + "Browsers" + FSC + "chromedriver.exe" );
		WebDriver driver = new ChromeDriver();
		//driver.get("http://demowebshop.tricentis.com/computers");
		
		driver.get("https://demoqa.com/html-contact-form/");
		driver.findElement(By.xpath("//input[@class='firstname']")).sendKeys("Honey");
		//List<WebElement> lst = driver.findElements(By.xpath("//a[contains(text(),'Computers')]//following-sibling::ul[@class=\"sublist firstLevel\"]/li"));
		/*int sizeL = driver.findElements(By.xpath("(//a[contains(text(),'Computers') ])[1]//following-sibling::ul[@class=\"sublist firstLevel\"]/li/a")).size();
		
		List<WebElement> lem = driver.findElements(By.xpath("(//a[contains(text(),'Computers') ])[1]//following-sibling::ul[@class=\"sublist firstLevel\"]/li/a"));
		System.out.println("Size is "+sizeL);
		Actions a=new Actions(driver);
		
		WebElement nel =  driver.findElement(By.xpath("//ul[@class='top-menu']//li/a[contains(text(),'Computers')]"));
		a.moveToElement(nel).build().perform();
		Thread.sleep(2000);
		
		int i =1;
		for (WebElement e : lem) {
			
			System.out.println(i+ " item in the submenu is : "+e.getText());
			i++;
		}
		*/
		/*for(int i=1; i<=sizeL;i++) {
			String text = driver.findElements(By.xpath("(//a[contains(text(),'Computers') ])[1]//following-sibling::ul[@class=\"sublist firstLevel\"]/li")).get(i).getAttribute("href");
					System.out.println("Text is : "+text +  " and value of i is : "+i);
		}*/
		
	}

	@BeforeClass
	public void setUpBeforeClass() throws IOException {
		properties = new Properties();
		FileInputStream inStream = new FileInputStream("./resources/others.properties");
		properties.load(inStream);
	}
	
	

	@BeforeMethod
	public void setUp() {
		driver = DriverFactory.getDriver(DriverNames.CHROME);
		initializeTestObjects();
		testcaseName = this.getClass().getSimpleName();
		System.out.println("This is test name : " + testcaseName);
		test = initiateReport(testcaseName, 1);
		loginPOM = new LoginPOM(driver, test);
		profilePOM = new ProfilePOM(driver, test);
		myCourcesPOM = new MyCourcesPOM(driver, test);
		baseUrl = properties.getProperty("baseURL");
		openURL(driver, baseUrl);
		driver.manage().window().maximize();

		loginPOM.sendUserName("Test_1589");
		loginPOM.sendPassword("test1234");
		loginPOM.clickLoginBtn();
	}

	
	@Test(timeOut = 1000)
	public void timeOut() throws InterruptedException {
		System.out.println("before execution");
		Thread.sleep(2000);
		System.out.println("Executed");
	}
	
	@Test
	public void changePasswordTest() {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		myCourcesPOM.clickEditProfile();
		profilePOM.enterPassword("test1234");
		profilePOM.enterNewPassword("test1234");
		profilePOM.clickSaveChangesBtn();

	}

	@AfterMethod
	public void afterMethod()  {
		loginPOM.clickUserDropdown();
		loginPOM.clickLogoff();
		GenericMethods.wait(1);
		closeTest();
	}

}
