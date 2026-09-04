package com.example.framework.tests;

import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.*;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    public static final Logger log = Log.getLogger(CheckoutTest.class);

    @Test
    @Epic("Checkout flow")
    @Feature("User makes an order")
    @Story("User can complete the checkout flow")
    @Description("Verify user is able to complete the checkout flow from login to completing the order")
    @Severity(SeverityLevel.CRITICAL)
    public void userCanCompleteCheckoutFlow(){
        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();

        InventoryPage inventoryPage =  loginPage.login(Secrets.getUsername(), Secrets.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        CartPage cartPage = new CartPage(page, env);
        cartPage.clickCheckout();

        CheckoutStepOnePage stepOne = new CheckoutStepOnePage(page, env);
        stepOne.fillCheckoutInfo("Zalan", "QA", "CR0 1AA");

        CheckoutStepTwoPage stepTwo = new CheckoutStepTwoPage(page, env);
        Assert.assertEquals(stepTwo.getItemName(), "Sauce Labs Backpack");

        stepTwo.clickFinish();

        CheckoutCompletePage completePage = new CheckoutCompletePage(page, env);
        Assert.assertEquals(completePage.getCompleteMessage(), "Thank you for your order!");
    }

}
