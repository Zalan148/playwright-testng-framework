package com.example.framework.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Secrets {
    //private static final Dotenv dotenv = Dotenv.load();

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static String getUsername() {
        return dotenv.get("PLAYWRIGHT_USERNAME");
    }

    public static String getPassword(){
        return dotenv.get("PLAYWRIGHT_PASSWORD");
    }
}
