package com.example.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;

import java.util.Map;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> toMap(APIResponse response) {
        try {
            return mapper.readValue(response.text(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to Map", e);
        }
    }

    public static List<Map<String, Object>> toList(APIResponse response) {
        try {
            return mapper.readValue(response.text(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to List", e);
        }
    }

    public static <T> T toPojo(APIResponse response, Class<T> clazz) {
        try {
            return mapper.readValue(response.text(), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to POJO", e);
        }
    }

    public static String getString(APIResponse response, String key) {
        return toMap(response).get(key).toString();
    }

    public static int getInt(APIResponse response, String key) {
        return Integer.parseInt(toMap(response).get(key).toString());
    }

    public static boolean getBoolean(APIResponse response, String key) {
        return Boolean.parseBoolean(toMap(response).get(key).toString());
    }
}
