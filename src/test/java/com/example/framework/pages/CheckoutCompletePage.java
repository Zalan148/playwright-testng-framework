package com.example.framework.pages;

import com.example.framework.config.Environment;
import com.example.framework.core.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;


public class CheckoutCompletePage extends BasePage {

    private final Locator completeHeader = page.locator(".complete-header");

    private final Environment env;

    public CheckoutCompletePage(Page page, Environment env){
        super(page);
        this.env = env;
    }

    public void open(){
        page.navigate(env.getBaseURL() + "/checkout-complete");
    }

    public String getCompleteMessage() {
        return completeHeader.innerText().trim();
    }
}
