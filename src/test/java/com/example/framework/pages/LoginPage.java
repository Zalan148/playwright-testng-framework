package com.example.framework.pages;

import com.example.framework.config.Environment;
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

    private final Environment env;

    public LoginPage(Page page, Environment env) {
        super(page);
        this.env = env;
    }

    public void open(){
        page.navigate(env.getBaseURL());
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
        return new InventoryPage(page, env);
    }

    //UI checks
    public String getErrorMessage() {
        return text(errorMessage);
    }

}
