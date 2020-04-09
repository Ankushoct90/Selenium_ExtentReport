package com.training.utility;

import java.io.File;

public interface Driver {

	public static final char FSC = File.separatorChar;
	public static final String USER_DIR = System.getProperty("user.dir");
	
	// KEYS
	String CHROME = "webdriver.chrome.driver";
	String FIREFOX = "webdriver.gecko.driver";
	String IE = "webdriver.ie.driver";
	String PHANTOM = "phantomjs.binary.path";

	// PATH
	// String CHROME_PATH="C:\\Vaibhav\\Selenium Software\\chromedriver.exe";

	String CHROME_PATH = USER_DIR + FSC + "Browsers" + FSC + "chromedriver.exe";
	String FIREFOX_PATH = USER_DIR + FSC + "Browsers" + FSC ;
	String IE_PATH = USER_DIR + FSC + "Browsers" + FSC ;
	String PHANTOM_PATH = USER_DIR + FSC + "Browsers" + FSC ;

}