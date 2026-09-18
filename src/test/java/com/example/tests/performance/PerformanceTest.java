package com.example.tests.performance;

import com.example.framework.core.BaseTest;
import com.example.framework.pages.InventoryPage;
import com.example.framework.pages.LoginPage;
import com.example.framework.utils.PerformanceUtils;
import org.testng.annotations.Test;

public class PerformanceTest extends BaseTest {
    @Test
    public void inventoryPagePerformanceBudget() {

        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();

        InventoryPage inventoryPage =
                loginPage.login("standard_user", "secret_sauce");

        System.out.println("Running PerformanceTest only");

        // Measure time until the inventory UI is ready
        long start = System.currentTimeMillis();

        inventoryPage.waitForPageLoad();

        long inventoryUiReadyMs =
                System.currentTimeMillis() - start;

        // Collect, report and validate all performance metrics
        PerformanceUtils.assertPerformanceBudgets(
                page,
                inventoryUiReadyMs
        );
    }

}
