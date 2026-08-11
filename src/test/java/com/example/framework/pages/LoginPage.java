package com.example.framework.pages;

import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;

public class LoginPage extends BasePage {

    private static final Logger log = Log.getLogger(LoginPage.class);

    // locators (now using Locator objects)
    private final Locator usernameField = $("#user-name");
    private final Locator passwordField = $("#password");
    private final Locator loginButton = $("#login-button");
    private final Locator errorMessage = $("[data-test='error']");

    public LoginPage(Page page) {
        super(page);
    }

    //Actions
    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password){
        type(passwordField, password);
    }

    public void clickLogin(){
        click(loginButton);
    }

    @Step("Login with username: {0} and password: {1}")
    public InventoryPage login(String username, String password) {
        log.info("Logging with username {} and password {}", username, password);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new InventoryPage(page);
    }

    //UI checks
    public String getErrorMessage() {
        return text(errorMessage);
    }


}
