package com.example.framework.pages;

import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;

public class CheckoutStepOnePage extends BasePage {

    private static final Logger log = Log.getLogger(CheckoutStepOnePage.class);

    private final String firstNameField = "#first-name";
    private final String lastNameField = "#last-name";
    private final String postalCodeField = "#postal-code";
    private final String continueButton = "#continue";

    public CheckoutStepOnePage(Page page) {
        super(page);
    }

    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }

    public void enterLastName(String lastName){
        type(lastNameField, lastName);
    }

    public void enterPostalCode(String postalCode){
        type(postalCodeField, postalCode);
    }

    @Step("Click continue")
    public void clickContinue() {
        log.info("Clicking continue button");
        click(continueButton);
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
