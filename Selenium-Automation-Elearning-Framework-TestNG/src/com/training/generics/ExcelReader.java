package com.training.generics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;


public class ExcelReader {
	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;
	private InputStream inputStream = null;

	
	
	public ExcelReader(String filepath) {
		path = filepath;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet("TestCases");
			fis.close();
		} catch (Exception e) {
			System.out.println("Got an exception while reading the Excel file. " + path + "\nException is ==> " + e.getMessage());
			try {
				fis.close();
			} catch (Exception ex) {
				System.out.println("Got an exception while closing the file input stream of the Excel file. " + path + "\nException is ==> " + e.getMessage());
			}
		}
	}

	
	/**
	 * Constructor to read Excel file from input stream. It will initialize 'Excel Reader' with input stream.
	 * @param stream (Datatype: InputStream)
	 */
	public ExcelReader(InputStream stream) {
		inputStream = stream;
		try {
			workbook = new XSSFWorkbook(inputStream);
			sheet = workbook.getSheet("TestCases");
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	public int getRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}

	}

	
	
	public int getRowsInColumn(String sheetName, int col) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int rowCount = 1;
			while (!getCellData(sheetName, col, rowCount + 1).equals("")) {
				rowCount++;
				if (rowCount > sheet.getLastRowNum() + 1) {
					// throw ERROR
					return 0;

				}

			}
			rowCount = rowCount - 1;
			return rowCount;
		}

	}

	

	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);

			if (cell == null)
				return "";

			if (cell.getCellType() == Cell.CELL_TYPE_STRING || cell.getCellType() == Cell.CELL_TYPE_FORMULA)
				return cell.getStringCellValue();

			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());

		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	
	
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";

			if (cell.getCellType() == Cell.CELL_TYPE_STRING || cell.getCellType() == Cell.CELL_TYPE_FORMULA)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

				String cellText = String.valueOf(cell.getNumericCellValue());

				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	
	public String setCellData(ExcelReader xlsxReader, String sheetName, String testName, String colName, String data) {
		int writeDataRowNum, writeDataColNum;
		String coordinates = null;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			sheet = workbook.getSheetAt(index);
			int testNameRowNum = getTestNameRowNum(xlsxReader, sheetName, testName);
			writeDataColNum = getTitleColNum(xlsxReader, sheetName, 1, colName);
			sheet.autoSizeColumn(writeDataColNum);
			writeDataRowNum = testNameRowNum - 1;
			row = sheet.getRow(writeDataRowNum);
			if (row == null)
				row = sheet.createRow(writeDataRowNum);

			cell = row.getCell(writeDataColNum);
			if (cell == null)
				cell = row.createCell(writeDataColNum);

			// cell style
			CellStyle cs = workbook.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(data);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
			coordinates = String.valueOf(writeDataRowNum) + ":" + String.valueOf(writeDataColNum);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception while writing the generated data to the excel");
		}
		return coordinates;
	}

	
	
	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	
	public int getColumnCount(String sheetName) {
		// check if sheet exists
		if (!isSheetExist(sheetName))
			return -1;

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();

	}

	public int getCellRowNum(String sheetName, String colName, String cellValue) {
		for (int i = 1; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		return -1;

	}

	
	public int getTestNameRowNum(ExcelReader xlsReader, String sheetName, String testCaseName) {
		int testStartRowNum = 1;
		int colNum = -1;
		if (sheetName.equals(""))//HybrisConstants.TESTCASES_SHEET
			colNum = 1;
		else if (sheetName.equals(""))//HybrisConstants.TESTDATA_SHEET
			colNum = 0;

		int maxRows = xlsReader.getRowCount(sheetName);
		while (!xlsReader.getCellData(sheetName, colNum, testStartRowNum).equals(testCaseName)) {
			testStartRowNum++;
			if (testStartRowNum > maxRows) {
				throw new Error("Error!!! Testcase name is not found in the excel in " + sheetName + " tab");
			}
		}
		return testStartRowNum;
	}

	
	public int getTitleColNum(ExcelReader xlsReader, String sheetName, int rowNum, String columnName) {
		int colNum = 0;
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNum - 1);
		int maxColumns = row.getPhysicalNumberOfCells();
		while (!xlsReader.getCellData(sheetName, colNum, rowNum).equals(columnName.trim())) {
			colNum++;
			if (colNum > maxColumns) {
				throw new Error(
						"Error!!! Column name '" + columnName + "' is not found in the excel in " + sheetName + " tab");
			}
		}
		return colNum;

	}

}
