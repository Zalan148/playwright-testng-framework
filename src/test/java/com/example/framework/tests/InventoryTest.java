package com.example.framework.tests;

import com.example.framework.components.DropdownComponent;
import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.core.Log;
import com.example.framework.pages.InventoryPage;
import com.example.framework.pages.LoginPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class InventoryTest extends BaseTest {

    private static final Logger log = Log.getLogger(InventoryTest.class);

    @Test
    @Description("Verify an item added to the cart increases the cart count")
    public void addItemToCartIncreasesCartCount() {
        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();
        InventoryPage inventoryPage = loginPage.login(Secrets.getUsername(), Secrets.getPassword());
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        assertEquals(inventoryPage.getCartCount(), 1, "Cart count should be 1 after adding one item");
    }

    @Test
    @Description("Verify products can be sorted alphabetically")
    public void productsSortAlphabetically() {
        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();
        InventoryPage inventoryPage = loginPage.login(Secrets.getUsername(), Secrets.getPassword());
        DropdownComponent sort = inventoryPage.getSortDropdown();
        sort.selectByValue("az");
        List<String> names = inventoryPage.getAllProductNames();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);
        assertEquals(names, sorted);
    }


}
