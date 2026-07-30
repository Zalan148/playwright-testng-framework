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
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(ConfigManager.getBaseUrl());
        loginPage.login("wrong_user", "wrong_password");
        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"), "Error message did not match expected text");
    }

    @Test
    public void validLoginNavigatesToInventoryPage(){
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(ConfigManager.getBaseUrl());
        loginPage.login(Secrets.getUsername(), Secrets.getPassword());
        Assert.assertTrue(page.url().contains("inventory.html"), "User did not navigate to inventory page after login");
    }



}
