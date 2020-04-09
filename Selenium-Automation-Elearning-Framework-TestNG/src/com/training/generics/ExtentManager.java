package com.training.generics;

import java.io.File;
import java.util.Date;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ExtentManager {
	private static ExtentReports extent;

	/**
	 * Method to initialize ExtentReports(3.0) object
	 * 
	 * @return returns an object of ExtentReports API
	 */
	public static ExtentReports getInstance() {
		if (extent == null) {
			Date date1 = new Date();
			String todaysFolder = GenericMethods.createFolder(ResourceConstants.EXTENT_REPORTS_PATH, date1);
			String fileName = date1.toString().replace(":", "_").replace(" ", "_") + ".html";
			String reportPath = todaysFolder + File.separatorChar + fileName;
			/*String reportPath = System.getProperty("user.dir") + File.separatorChar + "ExtentReports"
					+ File.separatorChar + fileName;*/
			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
			//System.out.println("Path is : " + reportPath);
			extent = new ExtentReports();
			// htmlReporter.setAppendExisting(true);
			// htmlReporter.loadXMLConfig(ResourceConstants.EXTENT_REPORTS_CONFIG);
			extent.attachReporter(htmlReporter);

			// ************************** New Code

			/*
			 * Date date1 = new Date();
			 * 
			 * String folderPathExtentReports = "./ExtentReports/";
			 * 
			 * String fileName = date1.toString().replace(":", "_").replace(" ", "_") +
			 * ".html";
			 * 
			 * String finalExtentReportPath = folderPathExtentReports + fileName;
			 * 
			 * ExtentHtmlReporter htmlReporter = new
			 * ExtentHtmlReporter(finalExtentReportPath);
			 * 
			 * extent = new ExtentReports();
			 * 
			 * //htmlReporter.setAppendExisting(true);
			 * 
			 * extent.attachReporter(htmlReporter);
			 */

		}
		return extent;
	}
}
