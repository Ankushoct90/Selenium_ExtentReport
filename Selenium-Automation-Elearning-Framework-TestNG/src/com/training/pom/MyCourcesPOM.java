package com.training.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.training.generics.GenericMethods;

public class MyCourcesPOM extends GenericMethods{
		
	public MyCourcesPOM(WebDriver driver,ExtentTest test) {
		super(driver,test);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//div[@class='normal-message']")
	private WebElement loginMessage;
	
	@FindBy(xpath = "//a[contains(text(),'Edit profile')]")
	private WebElement editProfile;
	
	public String getLoginMessage() {
		return loginMessage.getText();
	}
	
	public void clickEditProfile() {
		editProfile.click();
	}


	

}
