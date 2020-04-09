package com.training.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.training.generics.GenericMethods;

public class LoginPOM extends GenericMethods {
	
	public LoginPOM(WebDriver driver,ExtentTest test) {
		super(driver,test);
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "login")
	private WebElement userName;

	@FindBy(id = "password")
	private WebElement password;

	@FindBy(id = "form-login_submitAuth")
	private WebElement loginBtn;

	@FindBy(xpath = "//a[contains(text(),'Sign up!')]")
	private WebElement signUp;

	@FindBy(xpath = "//a[@class='dropdown-toggle']")
	private WebElement userDropdown;

	@FindBy(id = "logout_button")
	private WebElement logoff;

	public void sendUserName(String userName) {
		/*
		 * this.userName.clear(); this.userName.sendKeys(userName);
		 */
		enterText(driver,this.userName, userName);
	}

	public void sendPassword(String password) {
		/*
		 * this.password.clear(); this.password.sendKeys(password);
		 */
		enterText(driver,this.password, password);
	}

	public void clickLoginBtn() {
		// this.loginBtn.click();
		clickOnElement(driver,this.loginBtn);
	}

	public void clickSignUp() {
		//this.signUp.click();
		clickOnElement(driver,this.signUp);
	}

	public void clickUserDropdown() {
		//this.userDropdown.click();
		clickOnElement(driver,this.userDropdown);
	}

	public void clickLogoff() {
		//this.logoff.click();
		clickOnElement(driver,this.logoff);
	}
	
	
}
