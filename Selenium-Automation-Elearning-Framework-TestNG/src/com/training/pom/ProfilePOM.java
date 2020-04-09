package com.training.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.training.generics.GenericMethods;

public class ProfilePOM extends GenericMethods {

	public ProfilePOM(WebDriver driver,ExtentTest test) {
		super(driver,test);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "profile_password0")
	private WebElement password;

	@FindBy(id = "password1")
	private WebElement newPassword;

	@FindBy(id = "profile_password2")
	private WebElement confirmPassword;

	@FindBy(id = "profile_apply_change")
	private WebElement saveChangesBtn;

	public void enterPassword(String password) {
		/*
		 * this.password.clear(); this.password.sendKeys(password);
		 */
		enterText(driver,this.password, password);
	}

	public void enterNewPassword(String newPassword) {
		/*
		 * this.newPassword.clear(); this.newPassword.sendKeys(newPassword);
		 */

		enterText(driver,this.newPassword, newPassword);

		/*
		 * this.confirmPassword.clear(); this.confirmPassword.sendKeys(newPassword);
		 */
		enterText(driver,this.confirmPassword, newPassword);
	}

	public void clickSaveChangesBtn() {
		// this.saveChangesBtn.click();
		clickOnElement(driver,this.saveChangesBtn);
	}

}
