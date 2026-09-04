package com.example.framework.utils;

import com.microsoft.playwright.APIResponse;

public class ResponseValidator {
    public static void assertStatus(APIResponse response, int expected) {
        if (response.status() != expected) {
            throw new AssertionError("Expected status " + expected + " but got " + response.status());
        }
    }

    public static void assertKeyExists(APIResponse response, String key) {
        if (!JsonUtils.toMap(response).containsKey(key)) {
            throw new AssertionError("Key '" + key + "' not found in response");
        }
    }

    public static void assertString(APIResponse response, String key, String expected) {
        String actual = JsonUtils.getString(response, key);
        if (!actual.equals(expected)) {
            throw new AssertionError(
                    "Expected '" + key + "' to be '" + expected + "' but got '" + actual + "'"
            );
        }
    }

    public static void assertInt(APIResponse response, String key, int expected) {
        int actual = JsonUtils.getInt(response, key);
        if (actual != expected) {
            throw new AssertionError(
                    "Expected '" + key + "' to be " + expected + " but got " + actual
            );
        }
    }
}
