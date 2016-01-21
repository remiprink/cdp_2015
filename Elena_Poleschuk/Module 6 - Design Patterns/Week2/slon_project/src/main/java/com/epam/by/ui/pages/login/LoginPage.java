package com.epam.by.ui.pages.login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.epam.by.ui.pages.AbstractPage;
import com.epam.by.ui.pages.LogisticsAndSettingsPage;

public class LoginPage extends AbstractPage implements ILogin {

	@FindBy(id = "j_username")
	private WebElement loginInput;

	@FindBy(id = "j_password")
	private WebElement passwordInput;

	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(this.driver, this);
	}

	public LogisticsAndSettingsPage logIn(String userName, String userPassword) {
		loginInput.sendKeys(userName);
		passwordInput.sendKeys(userPassword);
		passwordInput.submit();
		return PageFactory.initElements(driver, LogisticsAndSettingsPage.class);
	}

}
