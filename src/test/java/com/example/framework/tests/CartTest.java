package com.example.framework.tests;

import com.example.framework.config.ConfigManager;
import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.CartPage;
import com.example.framework.pages.InventoryPage;
import com.example.framework.pages.LoginPage;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    private static final Logger log = Log.getLogger(CartTest.class);

    @Test
    public void addedItemAppearsInCart(){
        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        CartPage cartPage = new CartPage(page);
        page.navigate(ConfigManager.getBaseUrl());

        loginPage.login(Secrets.getUsername(), Secrets.getPassword());

        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.openCart();

        var items = cartPage.getCartItemNames();
        Assert.assertTrue(items.contains("Sauce Labs Backpack"));

    }
}
