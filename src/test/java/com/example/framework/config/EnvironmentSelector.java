package com.example.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.InputStream;

public class EnvironmentSelector {
    private static final String DEFAULT_ENV = "local";

    public static Environment load() {
        try {
            // 1. Read ENV from system property or environment variable
            String envName = System.getProperty("env",
                    System.getenv().getOrDefault("ENV", DEFAULT_ENV));

            // 2. Build the path to the JSON file
            String fileName = String.format("config/env.%s.json", envName);

            // 3. Load JSON from resources
            InputStream is = EnvironmentSelector.class.getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new RuntimeException("Environment file not found: " + fileName);
            }

            // 4. Deserialize JSON into Environment object
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, Environment.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load environment configuration", e);
        }
    }
}
