package com.example.framework.core;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public abstract class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected Locator $(String selector){
        return page.locator(selector);
    }

    protected void waitForLoad(){
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected void waitForVisible(Locator locator){
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }


    //click element
    protected void click(Locator locator){
        waitForVisible(locator);
        locator.click();
    }

    //Type text
    protected void type(Locator locator, String text) {
        waitForVisible(locator);
        locator.fill(text);
    }

    //Get text
    protected String text(Locator locator) {
        waitForVisible(locator);
        return locator.innerText();
    }


}
