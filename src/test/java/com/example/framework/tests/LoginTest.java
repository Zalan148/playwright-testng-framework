package com.example.framework.tests;

import com.example.framework.config.ConfigManager;
import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.LoginPage;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private static final Logger log = Log.getLogger(LoginTest.class);

    @Test
    public void invalidLoginShowsErrorMessage() {

        //Navigate to login page
        page.navigate(ConfigManager.getBaseUrl());

        //Create page object
        LoginPage loginPage = new LoginPage(page);

        //Perform invalid login
        loginPage.login("wrong_user", "wrong_password");

        //Assert error message
        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"), "Error message did not match expected text");
    }

    @Test
    public void validLoginNavigatesToInventoryPage(){

        //Navigate to login page
        page.navigate(ConfigManager.getBaseUrl());

        //Create page object
        LoginPage loginPage = new LoginPage(page);

        //Perform valid login
        loginPage.login(Secrets.getUsername(), Secrets.getPassword());

        //Assert navigation
        Assert.assertTrue(page.url().contains("inventory.html"), "User did not navigate to inventory page after login");
    }



}
