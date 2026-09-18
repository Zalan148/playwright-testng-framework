package com.example.framework.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;


public class PerformanceUtils {

    private static final Logger log = LoggerFactory.getLogger(PerformanceUtils.class);

    // Default performance budgets
    private static final long INVENTORY_UI_READY_BUDGET_MS = 3000;
    private static final long DOM_CONTENT_LOADED_BUDGET_MS = 2000;
    private static final long LOAD_EVENT_BUDGET_MS = 3000;
    private static final long JS_HEAP_BUDGET_MB = 500;

    /**
     * Measures performance metrics, logs the results to the console,
     * attaches the results to Allure, and validates the default budgets.
     */
    public static void assertPerformanceBudgets(Page page, long inventoryUiReadyMs) {

        // DOM Content Loaded
        Number domContentLoaded = (Number) page.evaluate(
                "() => performance.timing.domContentLoadedEventEnd - performance.timing.navigationStart"
        );
        long domContentLoadedMs = domContentLoaded.longValue();

        // Full Load Event
        Number loadEvent = (Number) page.evaluate(
                "() => performance.timing.loadEventEnd - performance.timing.navigationStart"
        );
        long loadEventMs = loadEvent.longValue();

        // Memory usage (Chrome only, experimental)
        Number jsHeapUsedSize = (Number) page.evaluate(
                "() => performance.memory ? performance.memory.usedJSHeapSize : 0"
        );
        long jsHeapUsedMB = jsHeapUsedSize.longValue() / 1024 / 1024;


        // Console output
        log.info("========== PERFORMANCE RESULTS ==========");
        log.info("Inventory UI Ready: {} ms (budget < {} ms)",
                inventoryUiReadyMs, INVENTORY_UI_READY_BUDGET_MS);
        log.info("DOM Content Loaded: {} ms (budget < {} ms)",
                domContentLoadedMs, DOM_CONTENT_LOADED_BUDGET_MS);
        log.info("Load Event: {} ms (budget < {} ms)",
                loadEventMs, LOAD_EVENT_BUDGET_MS);
        log.info("JS Heap Used: {} MB (budget < {} MB)",
                jsHeapUsedMB, JS_HEAP_BUDGET_MB);
        log.info("=========================================");

        // Allure attachment
        String performanceResults =
                "Inventory UI Ready: " + inventoryUiReadyMs + " ms"
                        + " (budget < " + INVENTORY_UI_READY_BUDGET_MS + " ms)\n"
                        + "DOM Content Loaded: " + domContentLoadedMs + " ms"
                        + " (budget < " + DOM_CONTENT_LOADED_BUDGET_MS + " ms)\n"
                        + "Load Event: " + loadEventMs + " ms"
                        + " (budget < " + LOAD_EVENT_BUDGET_MS + " ms)\n"
                        + "JS Heap Used: " + jsHeapUsedMB + " MB"
                        + " (budget < " + JS_HEAP_BUDGET_MB + " MB)";

        Allure.addAttachment(
                "Performance Metrics",
                "text/plain",
                performanceResults,
                ".txt"
        );

        // Performance budget assertions
        Assert.assertTrue( inventoryUiReadyMs < INVENTORY_UI_READY_BUDGET_MS,
                "Inventory UI ready exceeded budget: " + inventoryUiReadyMs + "ms");

        Assert.assertTrue( domContentLoadedMs < DOM_CONTENT_LOADED_BUDGET_MS,
                "DOM Content Loaded exceeded budget: " + domContentLoadedMs + "ms");

        Assert.assertTrue( loadEventMs < LOAD_EVENT_BUDGET_MS,
                "Full load exceeded budget: " + loadEventMs + "ms");

        Assert.assertTrue( jsHeapUsedMB < JS_HEAP_BUDGET_MB,
                "Memory usage exceeded budget: " + jsHeapUsedMB + " MB");
    }

    /**
     * Allows custom performance budgets for individual tests.
     */
    public static void assertPerformanceBudgets(
            Page page,
            long inventoryUiReadyMs,
            long inventoryUiReadyBudgetMs,
            long domBudgetMs,
            long loadBudgetMs,
            long heapBudgetMb) {

        // DOM Content Loaded
        Number domContentLoaded = (Number) page.evaluate(
                "() => performance.timing.domContentLoadedEventEnd - performance.timing.navigationStart"
        );
        long domContentLoadedMs = domContentLoaded.longValue();

        // Full Load Event
        Number loadEvent = (Number) page.evaluate(
                "() => performance.timing.loadEventEnd - performance.timing.navigationStart"
        );
        long loadEventMs = loadEvent.longValue();

        // Memory usage
        Number jsHeapUsedSize = (Number) page.evaluate(
                "() => performance.memory ? performance.memory.usedJSHeapSize : 0"
        );
        long jsHeapUsedMB = jsHeapUsedSize.longValue() / 1024 / 1024;

        // Console output
        log.info("========== PERFORMANCE RESULTS ==========");
        log.info("Inventory UI Ready: {} ms (budget < {} ms)",
                inventoryUiReadyMs, inventoryUiReadyBudgetMs);
        log.info("DOM Content Loaded: {} ms (budget < {} ms)",
                domContentLoadedMs, domBudgetMs);
        log.info("Load Event: {} ms (budget < {} ms)",
                loadEventMs, loadBudgetMs);
        log.info("JS Heap Used: {} MB (budget < {} MB)",
                jsHeapUsedMB, heapBudgetMb);
        log.info("=========================================");

        // Allure attachment
        String performanceResults =
                "Inventory UI Ready: " + inventoryUiReadyMs + " ms"
                        + " (budget < " + inventoryUiReadyBudgetMs + " ms)\n"
                        + "DOM Content Loaded: " + domContentLoadedMs + " ms"
                        + " (budget < " + domBudgetMs + " ms)\n"
                        + "Load Event: " + loadEventMs + " ms"
                        + " (budget < " + loadBudgetMs + " ms)\n"
                        + "JS Heap Used: " + jsHeapUsedMB + " MB"
                        + " (budget < " + heapBudgetMb + " MB)";

        Allure.addAttachment(
                "Performance Metrics",
                "text/plain",
                performanceResults,
                ".txt"
        );

        // Performance budget assertions
        assert inventoryUiReadyMs < inventoryUiReadyBudgetMs :
                "Inventory UI ready exceeded budget: "
                        + inventoryUiReadyMs + "ms";

        assert domContentLoadedMs < domBudgetMs :
                "DOM Content Loaded exceeded budget: "
                        + domContentLoadedMs + "ms";

        assert loadEventMs < loadBudgetMs :
                "Full load exceeded budget: "
                        + loadEventMs + "ms";

        assert jsHeapUsedMB < heapBudgetMb :
                "Memory usage exceeded budget: "
                        + jsHeapUsedMB + " MB";
    }
}
