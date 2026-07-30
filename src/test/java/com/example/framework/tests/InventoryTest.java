package com.example.framework.tests;

import com.example.framework.config.ConfigManager;
import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.InventoryPage;
import com.example.framework.pages.LoginPage;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InventoryTest extends BaseTest {

    private static final Logger log = Log.getLogger(InventoryTest.class);

    @Test
    public void addItemToCartIncreasesCartCount() {
        LoginPage loginPage = new LoginPage(page);
        InventoryPage inventoryPage = new InventoryPage(page);
        loginPage.navigate(ConfigManager.getBaseUrl());
        loginPage.login(Secrets.getUsername(), Secrets.getPassword());
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        Assert.assertEquals(inventoryPage.getCartCount(), 1, "Cart count should be 1 after adding one item");
    }
}
