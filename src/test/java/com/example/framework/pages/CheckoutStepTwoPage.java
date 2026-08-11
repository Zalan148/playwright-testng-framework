package com.example.framework.pages;

import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;

public class CheckoutStepTwoPage extends BasePage {

    private static final Logger log = Log.getLogger(CheckoutStepTwoPage.class);

    private final Locator finishButton = page.locator("#finish");
    private final Locator itemName = page.locator(".inventory_item_name");


    public CheckoutStepTwoPage(Page page) {
        super(page);
    }

    public String getItemName(){
        return itemName.innerText().trim();
    }

    @Step("Click finish")
    public void clickFinish(){
        log.info("Clicking finish button" );
        finishButton.click();
    }
}
