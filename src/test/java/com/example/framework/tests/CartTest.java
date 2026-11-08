package com.example.framework.tests;

import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.CartPage;
import com.example.framework.pages.InventoryPage;
import com.example.framework.pages.LoginPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    private static final Logger log = Log.getLogger(CartTest.class);

    @Test
    @Description("Verify user is able to add an item to the cart")
    public void addedItemAppearsInCart(){
        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();
        InventoryPage inventoryPage = loginPage.login(Secrets.getUsername(), Secrets.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        CartPage cartPage = new CartPage(page, env);
        var items = cartPage.getCartItemNames();
        Assert.assertTrue(items.contains("Sauce Labs Backpack"));

    }
}
