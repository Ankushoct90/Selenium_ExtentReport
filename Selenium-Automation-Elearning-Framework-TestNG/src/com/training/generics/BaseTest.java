package com.training.generics;

import java.net.URL;
import java.util.Hashtable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class BaseTest {

	public GenericMethods genericMethods;

	public static ExtentTest test;
	public Hashtable<String, String> data;
	public static ExtentReports extent;

	// public ExcelReader xls = new ExcelReader(ResourceConstants.APPDATASHEET);//
	// add Excel Read code before using it

	public WebDriver driver = null;
	public String testcaseName = null;
	public String shortTestName = null;
	public int count;
	public int testResult;

	/**
	 * Method to initialize ExtentTest report
	 * 
	 * @param testName
	 *            : This parameter gives the test case Name (datatype: String)
	 * @param itr
	 *            : This parameter gives the iteration of test run (datatype : int)
	 * @return test : An instance of ExtentReport API
	 */
	public ExtentTest initiateReport(String testName, int itr) {

		/*
		 * if (!DataUtil.isTestExecutable(xls, testcaseName)) { //
		 * skipTestRun("Skipping the test as Test case Runmode is set to 'N' for the given suite"
		 * ); }
		 */
		extent = ExtentManager.getInstance();
		test = extent.createTest(testName + " - iteration # " + itr);
		// System.out.println("*********" + testName + " - iteration # " + itr +
		// "********* INSIDE INITAIRE METHOD");
		test.info("Test case execution started of the test case : " + testName);

		return test;
	}

	/**
	 * This method initializes the remote Web Driver
	 * 
	 * @param dCapabilities
	 *            (Datatype: DesiredCapabilies)
	 * 
	 * @return an object of WebDriver (Datatype: WebDriver)
	 */
	public WebDriver openRemoteBrowser(DesiredCapabilities dCapabilities) {
		WebDriver driver2 = null;
		try {
			driver2 = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dCapabilities);
		} catch (Exception e) {
			e.getStackTrace();
			throw new Error("Got an exception while starting the Remote driver session in openRemoteBrowser method");
		}
		return driver2;
	}

	/**
	 * Instantiating all the test objects
	 */
	public void initializeTestObjects() {
		genericMethods = new GenericMethods(driver, test);

	}

	/**
	 * This method is for opening test URL
	 * 
	 * @param driver1
	 *            :This is an instance which implements WebDriver
	 * @param link
	 *            : This parameter is for passing Test URL link (Datatype : String)
	 */
	public void openURL(WebDriver driver1, String link) {
		driver1.get(link);
		GenericMethods.waitForPageToLoad(driver1);

	}

	/**
	 * This method is for capturing Screenshot
	 * 
	 * @param driver1
	 *            instance of WebDriver
	 * 
	 *            public void captureScreenShot(WebDriver driver1) {
	 * 
	 *            if (driver1.equals(driverFS)) { if (driverFS != null)
	 *            base_TO_FS.takeExtraScreenshot(driverFS, shortTestName); } else if
	 *            (driver1.equals(driverBO)) { if (driverBO != null)
	 *            base_TO_BO.takeExtraScreenshot(driverBO, shortTestName); }
	 * 
	 *            }
	 */

	/**
	 * This method is to close all session in FS and BO
	 * 
	 * @param driver
	 *            : This is an instance which implements WebDriver
	 * @param store2
	 *            : This is the parameter where we check for "FS" or "BO"
	 */
	public void quitSession(WebDriver driver) {
		if (driver != null) {
			try {
				driver.quit();

			} catch (Exception e) {

				genericMethods.testInfo("Exception while quitting the browser. It might have been died earlier!!!");

			}
		}

	}

	/**
	 * This method close the session
	 */
	public void closeTest() {
		closeSession(driver);
		//finalTestResult(testResult, test);
		if (extent != null)
			extent.flush();

	}

	/**
	 * This method is to close all open sessions
	 * 
	 * @param driver1
	 *            : This is an instance to implement WebDriver
	 * @param store
	 *            : This is to store Session ID (datatype : String)
	 */
	public void closeSession(WebDriver driver1) {
		try {
			if (driver1 != null)
				quitSession(driver1);
		} catch (Exception e) {

		}
	}

	/**
	 * This method gives the final test run result
	 * 
	 * @param result
	 *            : This is to give test result (dataType: int)
	 * @param test1
	 *            :This is an object for ExtentTest API
	 */
	public void finalTestResult(int result, ExtentTest test1) {
		try {
			GenericMethods.finalTestStatus(result, test1);
		} catch (Exception e) {

		}
	}

}
