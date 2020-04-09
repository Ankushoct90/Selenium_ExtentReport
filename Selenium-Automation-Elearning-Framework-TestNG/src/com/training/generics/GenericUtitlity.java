package com.training.generics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class GenericUtitlity {
	static WebDriver driver;
	public ExcelReader xls; // = new ExcelReader("");
	public int count;
	public String testcaseName = null;
	public String testName = "This is a sample Test";
	public int testResult;
	public int endTestResult = 0;
	public static ExtentReports extent;
	public WebDriverWait wait;
	public static ExtentTest test;
	public static final String SCREENSHOT_PATH = "." + File.separatorChar + "Reports";

	public GenericUtitlity() {
		System.out.println("Printed first");
		//extent = ExtentManager.getInstance();
		System.out.println(testName + "tinks");
		//test = extent.createTest("Selenium reporting using Extent Reports");
		System.out.println("Extent report invoked");
	}

	public void openURL(WebDriver driver1, String link) {
		driver1.get(link);
		driver1.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void takeScreenshot(WebDriver driver) {

		Date date = new Date();
		String USER_DIR = System.getProperty("user.dir");

		try {
			String filePath = USER_DIR + "\\screenshot\\" + date.toString().replace(":", "_").replace(" ", "_")
					+ ".png";
			System.out.println("this is file path" + filePath);
			String screenPath = "." + File.separatorChar + "screenshot" + File.separatorChar + filePath;
			System.out.println("This is screenpath" + screenPath);
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File screenFile = scrShot.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenFile, new File(filePath));
			test.addScreenCaptureFromPath(filePath);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("breaker");
			e.printStackTrace();
		}

	}

	public boolean isElementPresent(WebDriver driver, WebElement expectedElement) {
		boolean isPresent = false;
		try {
			if (expectedElement.isDisplayed() || expectedElement.isEnabled()) {
				isPresent = true;
			} else {
				takeScreenshot(driver);
				test.info("Warning Message: Element is Not Present. Trying to find element ="
						+ expectedElement.toString());
			}
		} catch (Exception exception) {
			takeScreenshot(driver);
			test.info("Got Exception:'" + exception.getMessage()
					+ "' Warning Message: Element is Not Present. Trying to find element ="
					+ expectedElement.toString());
			return isPresent;
		}
		return isPresent;
	}

	public boolean waitUntillElementIsClickable(WebDriver driver, WebElement element, int waitTill) {
		try {
			wait = new WebDriverWait(driver, waitTill);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public boolean waitUntilElementVisibility(WebDriver driver, WebElement element, int waitTill) {
		try {
			wait = new WebDriverWait(driver, waitTill);
			wait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public void clickWithJavaScript(WebDriver driver1, WebElement element) {
		waitUntilElementVisibility(driver1, element, 10);
		JavascriptExecutor executor = (JavascriptExecutor) driver1;
		executor.executeScript("arguments[0].click();", element);
	}

	public void clickOnElement(WebDriver driver, WebElement elementToClick) {
		if (isElementPresent(driver, elementToClick)) {
			if (waitUntillElementIsClickable(driver, elementToClick, 5000)) {
				test.info("Expected Element Found and Clickable '" + elementToClick.toString() + "'");
				clickWithJavaScript(driver, elementToClick);
			} else {
				reportFailure(
						"Element was Found and Displayed But it is not Clickable '" + elementToClick.toString() + "'",
						driver);
			}
		} else {
			reportFailure("Element was not Found, hence unable to click '" + elementToClick.toString() + "'", driver);
		}
	}

	public String getTextFromElement(WebElement element, WebDriver driver) {
		String textvalue = null;
		try {
			if (isElementPresent(driver, element)) {
				textvalue = element.getText();
				textvalue = textvalue.trim();
			} else
				test.error("Element is not present: " + element.toString());
		} catch (Exception e) {
			reportFailure("Got an exception while fetching text from an element.Exception is: " + e.getMessage(),
					driver);
		}
		return textvalue;
	}

	public boolean verifyTextContentFromElement(String expectedTextOrError, WebElement element) {

		test.info("Retrieving text content from an element");
		boolean isPresent = false;

		try {
			if (getTextFromElement(element, driver).contains(expectedTextOrError)) {
				reportPass("Expected text/error content was present", driver);
				isPresent = true;
			}
		} catch (Exception e) {
			reportFailure("Expected text/error content was not present" + "Actual text =" + element.getText()
					+ "Expected Text =" + expectedTextOrError, driver);
		}
		return isPresent;
	}

	public void reportPass(String passMessage, WebDriver driver) {
		test.pass(passMessage);
		takeScreenshot(driver);
	}

	public void reportFailure(String failureMessage, WebDriver driver) {
		test.fail(failureMessage);
		takeScreenshot(driver);
		extent.flush();
		Assert.fail(failureMessage);
	}

	public void enterText(WebDriver driver1, WebElement element, String val) {
		try {

			element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			element.sendKeys(Keys.DELETE);
			element.sendKeys(val);
		} catch (Exception e) {
			reportFailure("Exception occurred while entering text in the field " + element.toString()
					+ "Exception Description: " + e.getMessage(), driver);
		}
	}

	public static String createFolder(String folderPath, Date date1) {
		String folderName;
		SimpleDateFormat sdtf = new SimpleDateFormat("dd-MMM-YY");
		folderName = folderPath + File.separatorChar + sdtf.format(date1);
		// create a directory in the path
		createFolder(folderName);
		return folderName;
	}

	public static void createFolder(String folderPath) {
		try {
			File file = new File(folderPath);
			if (!file.exists())
				file.mkdir();
		} catch (Exception e) {

		}
	}

	public ExtentTest initiateReport(String testName, int itr) {

		/*if (!DataUtil.isTestExecutable(xls, testcaseName)) {
			skipTestRun("Skipping the test as Test case Runmode is set to 'N' for the given suite");
		}*/
		System.out.println("INSIDE INITIATE REPORT METHOD");
		extent = ExtentManager.getInstance();
		test = extent.createTest(testName + " - iteration # " + itr);
		return test;
	}

	public static void finalTestStatus(int result, ExtentTest test1) {
		try {
			if (result == 1) {
				test1.pass("Test case is PASSED");
			} else if (result == 0) {
				test1.fail("Test case is FAILED");
			} else if (result == -1) {
				test1.error("ERROR in Test execution. Please re-run the test.");
			} else if (result == -2) {
				test1.skip("Test case is skipped as expected.");
			}
		} catch (Exception e) {
			test1.error("Got an exception while setting the final test status");
		}

	}

	public void skipTestRun(String msg) {
		testResult = -2;
		throw new SkipException(msg);
	}

}
