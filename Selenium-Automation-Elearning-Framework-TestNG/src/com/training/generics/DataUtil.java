package com.training.generics;

import java.util.Hashtable;

import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

public class DataUtil {
	static String testDataSheetName;
	static String testCaseSheetName;

	public static Object[][] getData(ExcelReader xls, String testCaseName, ExtentTest test1) {
		if (xls == null) {
			test1.info("Execle reader object is null");
			//GenericUtitlity.finalTestStatus(-1, test1);
			throw new Error();
		}
		testDataSheetName = "";
		testCaseSheetName = "";
		excelSheetCheck(xls, testCaseSheetName, test1);
		excelSheetCheck(xls, testDataSheetName, test1);
		testCaseNameCheck(xls, 1, testCaseName, testCaseSheetName, test1);
		testCaseNameCheck(xls, 0, testCaseName, testDataSheetName, test1);

		// reads data for only testCaseName
		int testStartRowNum = 1;
		while (!xls.getCellData(testDataSheetName, 0, testStartRowNum).equals(testCaseName)) {
			testStartRowNum++;
		}

		int colStartRowNum = testStartRowNum + 1;
		int dataStartRowNum = testStartRowNum + 2;

		// calculate rows of data
		int rows = 0;
		while (!xls.getCellData(testDataSheetName, 0, dataStartRowNum + rows).equals("")) {
			rows++;
		}

		// calculate total cols
		int cols = 0;
		while (!xls.getCellData(testDataSheetName, cols, colStartRowNum).equals("")) {
			cols++;
		}

		Object[][] data = new Object[rows][1];
		// read the data
		int dataRow = 0;
		Hashtable<String, String> table = null;
		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {
			table = new Hashtable<String, String>();
			for (int cNum = 0; cNum < cols; cNum++) {
				String key = xls.getCellData(testDataSheetName, cNum, colStartRowNum);
				String value = xls.getCellData(testDataSheetName, cNum, rNum);
				table.put(key, value);
			}
			data[dataRow][0] = table;
			dataRow++;
		}
		return data;
	}

	private static void excelSheetCheck(ExcelReader xls, String sheetName, ExtentTest test1) {

		if (!xls.isSheetExist(sheetName)) {
			test1.info("'" + sheetName + "' sheet does NOT exist in the excel file.");
			//GenericUtitlity.finalTestStatus(-1, test1);
			Assert.fail("'" + sheetName + "' sheet does NOT exist in the excel file.");
		}

	}

	public static void testCaseNameCheck(ExcelReader xlsx, int colNum, String testname, String sheetName,
			ExtentTest test) {
		int rows = xlsx.getRowCount(sheetName);
		int testNameRow = 1;
		while (!xlsx.getCellData(sheetName, colNum, testNameRow).equalsIgnoreCase(testname)) {
			testNameRow++;
			if (testNameRow > rows) {
				//GenericUtitlity.finalTestStatus(-1, test);
				throw new Error("Testcase name " + testname + " does not exist in the '" + sheetName + "' sheet");
			}
		}
	}

	public static boolean isTestExecutable(ExcelReader xls, String testCaseName) {
		int rows = xls.getRowCount("");
		for (int rNum = 1; rNum <= rows; rNum++) {
			String tcid = xls.getCellData("", "TCID", rNum);
			if (tcid.equals(testCaseName)) {
				String runmode = xls.getCellData("", "" + "Runmode", rNum);
				if (runmode.equals("Y"))
					return true;
				else
					return false;
			}
		}
		return false;
	}

}
