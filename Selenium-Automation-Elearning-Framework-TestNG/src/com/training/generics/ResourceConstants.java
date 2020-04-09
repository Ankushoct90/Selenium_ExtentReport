package com.training.generics;

import java.io.File;

public class ResourceConstants {

	static String machineOS = null;

	public static final char FSC = File.separatorChar;
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String SCREENSHOT_PATH = USER_DIR + FSC + "Screenshots";
	public static final String EXTENT_REPORTS_PATH = USER_DIR + FSC + "ExtentReports";
	public static final String EXTENT_REPORTS_CONFIG = USER_DIR + FSC + "extent-config.xml";
	public static final String GECKODRIVER_EXE = USER_DIR + FSC + "Browsers" + FSC + "geckodriver.exe";
	public static final String CHROMEDRIVER_EXE = USER_DIR + FSC + "Browsers" + FSC + "chromedriver.exe";

	public static final String MACHINE_OS = getMachineOSLowerCase();
	public static final String DEFAULT_MACHINE_OS = "Windows"; // Windows, Linux
	public static final String LINUX_CHROMEDRIVER = "/opt/google/chromedriver_2.43/chromedriver";

	public static final String TESTCASES_SHEET = "TestCases";

	public static final String APPDATASHEET = USER_DIR +  FSC + "resources" + FSC + "HybrisTestData_QA.xlsx";

	private static String getMachineOSLowerCase() {
		if (machineOS == null) {
			try {
				machineOS = System.getProperty("os"); // windows/linux
				machineOS = machineOS.toLowerCase();
				switch (machineOS) {
				case "linux":
				case "windows":
					break;
				default:
					throw new Error("INVALID machine OS is provided. Expected is windows/linux/macos");
				}
			} catch (Exception e) {
				machineOS = ResourceConstants.DEFAULT_MACHINE_OS;
				machineOS = machineOS.toLowerCase();
			}
		}
		return machineOS;
	}

}
