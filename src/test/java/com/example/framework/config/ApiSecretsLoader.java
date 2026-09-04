package com.example.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class ApiSecretsLoader {
    public static ApiSecrets load(String envName) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    new File("src/test/resources/config/secrets." + envName + ".json"),
                    ApiSecrets.class
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to load secrets for env: " + envName, e);
        }
    }
}
