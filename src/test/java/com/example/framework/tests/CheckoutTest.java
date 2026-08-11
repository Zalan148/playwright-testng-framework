package com.example.framework.tests;

import com.example.framework.config.ConfigManager;
import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.*;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    public static final Logger log = Log.getLogger(CheckoutTest.class);

    @Test
    public void userCanCompleteCheckoutFlow(){
        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);
        CheckoutStepOnePage stepOne = new CheckoutStepOnePage(page);
        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage(page);
        CheckoutCompletePage completePage = new CheckoutCompletePage(page);

        page.navigate(ConfigManager.getBaseUrl());
        loginPage.login(Secrets.getUsername(), Secrets.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        cartPage.clickCheckout();

        stepOne.fillCheckoutInfo("Zalan", "QA", "CR0 1AA");

        Assert.assertEquals(stepTwo.getItemName(), "Sauce Labs Backpack");

        stepTwo.clickFinish();

        Assert.assertEquals(completePage.getCompleteMessage(), "Thank you for your order!");
    }

}
