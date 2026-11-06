package com.example.framework.tests;

import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.LoginPage;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private static final Logger log = Log.getLogger(LoginTest.class);

    @Test
    @Description("Verify an error message appears when login is invalid")
    public void invalidLoginShowsErrorMessage() {

        //Create page object
        LoginPage loginPage = new LoginPage(page, env);

        //Use Page Object navigation
        loginPage.open();

        //Perform invalid login
        loginPage.login("wrong_user", "wrong_password");

        //Assert error message
        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"), "Error message did not match expected text");
    }

    @Test
    @Epic("Login")
    @Feature("Valid login details")
    @Story("Successful Login navigates to Inventory page")
    @Description("Verify able to login successfully and navigate to the inventory page")
    @Severity(SeverityLevel.CRITICAL)
    public void validLoginNavigatesToInventoryPage(){

        //Create page object
        LoginPage loginPage = new LoginPage(page, env);

        //Use Page Object navigation
        loginPage.open();

        var inventoryPage = loginPage.login(Secrets.getUsername(), Secrets.getPassword());

        //Assert navigation
        Assert.assertTrue(page.url().contains("inventory.html"), "User did not navigate to inventory page after login");
    }

}
