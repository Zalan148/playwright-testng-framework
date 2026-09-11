package com.example.tests.diagnostic;

import com.microsoft.playwright.Playwright;

public class PlaywrightStartupTest {
    public static void main(String[] args) {
        System.out.println("Before Playwright.create()");

        long start = System.currentTimeMillis();

        Playwright playwright = Playwright.create();

        long end = System.currentTimeMillis();

        System.out.println("After Playwright.create()");
        System.out.println("Playwright.create() took: " + (end - start) + " ms");

        playwright.close();
    }
}
