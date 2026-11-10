package com.example.framework.pages;

import com.example.framework.config.Environment;
import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;

public class CheckoutStepOnePage extends BasePage {

    private static final Logger log = Log.getLogger(CheckoutStepOnePage.class);

    private final Locator firstNameField = page.locator("#first-name");
    private final Locator lastNameField = page.locator("#last-name");
    private final Locator postalCodeField = page.locator("#postal-code");
    private final Locator continueButton = page.locator("#continue");

    private final Environment env;

    public CheckoutStepOnePage(Page page, Environment env) {
        super(page);
        this.env = env;
    }

    public void open(){
        page.navigate(env.getBaseURL() + "/checkout-step-one");
    }

    public void enterFirstName(String firstName) {
        firstNameField.fill(firstName);
    }

    public void enterLastName(String lastName){
        lastNameField.fill(lastName);
    }

    public void enterPostalCode(String postalCode){
        postalCodeField.fill(postalCode);
    }

    @Step("Click continue")
    public void clickContinue() {
        log.info("Clicking continue button");
        continueButton.click();
    }

    @Step("Fill checkout info: {0} {1} {2}")
    public void fillCheckoutInfo(String first, String last, String postal) {
        log.info("Filling checkout info: {} {} {}", first, last, postal);
        enterFirstName(first);
        enterLastName(last);
        enterPostalCode(postal);
        clickContinue();
    }
}
