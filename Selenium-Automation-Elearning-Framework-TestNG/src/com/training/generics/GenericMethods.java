package com.training.generics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * 
 * @author Naveen
 * @see this class will help when you want to do custom business logic, since in
 *      POM we dont do dynamic elements available, when you want to iterate the
 *      table/accordion etc
 * @since 17-Dec-2018
 */
public class GenericMethods {
	public WebDriver driver;
	public WebDriverWait wait;
	public ExtentTest test;
	public String testcaseName = null;
	public ExtentReports extent = ExtentManager.getInstance();

	public  GenericMethods(WebDriver driver,ExtentTest test) {
		this.driver = driver;
		this.test = test;
	}
	

	/**
	 * 
	 * @param locator
	 * @param type
	 * @see type is id, name, xpath, text, partialtext
	 * @see locator will be the element to be found on DOM
	 * @return WebElement this method shall give provided it has single enty in the
	 *         DOM
	 */
	public WebElement getElement(String locator, String type) {
		WebElement element = null;
		type = type.toLowerCase();

		if (type.equals("id")) {
			element = driver.findElement(By.id(locator));
		} else if (type.equals("css")) {
			element = driver.findElement(By.cssSelector(locator));
		} else if (type.equals("name")) {
			element = driver.findElement(By.name(locator));
		} else if (type.equals("xpath")) {
			element = driver.findElement(By.xpath(locator));
		}
		if (checkSingleEntry(locator, type)) {
			System.out.println("Element Found and Returned");
			return element;
		}
		System.out.println("Sorry Element not found, so not returned...");
		return null;

	}

	// shall give if it has multiple entries as a list in DOM

	public List<WebElement> getElementsAsList(String locator, String type) {
		type = type.toLowerCase();
		if (type.equals("id")) {
			return driver.findElements(By.id(locator));
		} else if (type.equals("name")) {
			return driver.findElements(By.name(locator));
		} else if (type.equals("xpath")) {
			return driver.findElements(By.xpath(locator));
		} else if (type.equals("class")) {
			return driver.findElements(By.className(locator));
		} // other TODO
		return null;
	}

	// return true if element exists
	// this method works for us when we have more than 1 element
	// to be found for
	public boolean isElementFound(String locator, String type) {
		return getElementsAsList(locator, type).size() > 0;
	}

	// this method gives true only where there is an single entry
	// in the DOM
	public boolean checkSingleEntry(String locator, String type) {
		return getElementsAsList(locator, type).size() == 1;
	}

	// ***************************** Generic Methods for webElements By Ankush

	public boolean waitUntilElementClickable(WebElement element, long waitTime) {
		try {
			wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (TimeoutException e) {
			return false;
		}

	}

	public boolean waitUntilElementIsDisplayed(WebElement element, long waitTime) {
		try {
			wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public void enterText(WebDriver driver,WebElement element, String value) {
		try {
			/*
			 * element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			 * element.sendKeys(Keys.DELETE);
			 */
			scrollToElement(element);
			element.clear();
			element.sendKeys(value);
			takeScreenShot(driver);
		} catch (Exception e) {
			testFail("Exception occurred while entering text in the field " + element.toString()
					+ "Exception Description: " + e.getMessage());
		}

	}

	public void clickOnElement(WebDriver driver,WebElement element) {
		if (waitUntilElementIsDisplayed(element, 10)) { // instead of 100 put a wait time from constant file

			if (waitUntilElementClickable(element, 10)) {
				
				//element.click();
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				executor.executeScript("arguments[0].click();");
				// Reporter.log("Clicked successfully on : " + element.toString());
				testPass("Clicked successfully on : " + element.toString());

			} else {

				testFail("Element is not clickable : " + element.toString());
			}

		} else {
			testFail("Element is not displayed : " + element.toString());
		}

	}

	public boolean isElementPresent(WebElement expectedElement) {
		boolean isPresent = false;
		try {
			if (expectedElement.isDisplayed() || expectedElement.isEnabled()) {
				isPresent = true;
			} else {

				testInfo("Warning Message: Element is Not Present. Trying to find element ="
						+ expectedElement.toString());
			}
		} catch (Exception exception) {

			testInfo("Got Exception:'" + exception.getMessage()
					+ "' Warning Message: Element is Not Present. Trying to find element ="
					+ expectedElement.toString());
			return isPresent;
		}
		return isPresent;
	}

	public void scrollToElement(WebElement element) {
		try {
			if (isElementPresent(element)) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			} else {
				testFail("Unable to scroll to element '" + element.toString());
			}
		} catch (Exception e) {
			testFail("Got an exception while scrolling to the Element. Element is " + element.toString());
		}
	}

	// ****************************************** Reporting generic methods

	public void testFail(String message) {
		test.fail(message);
		Assert.fail(message);
		takeScreenShot(driver);
	}

	public void testWarning(String message) {
		test.warning(message);
		takeScreenShot(driver);
	}

	public void testPass(String message) {
		test.pass(message);
		takeScreenShot(driver);
	}

	public void testInfo(String message) {
		test.info(message);
		takeScreenShot(driver);
	}

	public void testError(String message) {
		test.error(message);
		throw new Error(message
				+ "Error occurred while executing the test. Please re-run this test after fixing the setup/Test data etc.");
		// Screenshot method call -- MANDATORY
	}

	// ******************************** generic screenshot method

	public static void createFolder(String folderPath) {
		try {
			File file = new File(folderPath);
			if (!file.exists())
				file.mkdir();
		} catch (Exception e) {

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

	public void takeScreenShot(WebDriver driver1) {
		if (driver1 != null && test != null) {
			Date date = new Date();
			// create folder for today's date
			String todaysFolder = createFolder(ResourceConstants.SCREENSHOT_PATH, date);// add folder path
			todaysFolder = todaysFolder + File.separatorChar + "Screenshots";
			createFolder(todaysFolder);
			//String screenshotFile = date.toString().replace(":", "_").replace(" ", "_") + ".png";
			String screenshotFile = date.toString().replace(":", "_").replace(" ", "_") + ".jpeg";
			System.out.println("Attached screenshot path is "+screenshotFile);
			String filePath = todaysFolder + File.separatorChar + screenshotFile;
			String screenPath = System.getProperty("user.dir") + File.separatorChar + "Screenshots" + File.separatorChar + screenshotFile;
			// store screenshot in that file
			try {
				File scrFile = ((TakesScreenshot) driver1).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(filePath));
				//test.addScreenCaptureFromPath(screenPath, screenPath);
				test.addScreenCaptureFromPath(filePath, filePath);
			} catch (Exception e) {
				testWarning("Got an exception while capturing the screenshot");
			}

		} else
			throw new Error("Either driver or report object is 'Null'");
	}
	
	
	
	public static void wait(int timeToWaitInSec) {
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void waitForPageToLoad(WebDriver driver) {
		wait(1);
		String state = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
		int totalTime = 0;
		while (!state.equals("complete")) {
			wait(1);
			state = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
			totalTime++;
			if (totalTime > 50)
				Assert.fail("Page is not loaded successfully after waiting for long time.");
		}
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

}