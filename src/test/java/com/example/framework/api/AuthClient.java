package com.example.framework.api;

import com.example.framework.config.ApiSecrets;
import com.example.framework.config.Environment;
import com.microsoft.playwright.APIResponse;
import java.util.Map;

public class AuthClient extends BaseApiClient {
    public AuthClient(Environment env, ApiSecrets apiSecrets){
        super(env, apiSecrets);
    }

    public APIResponse login(String username, String password) {
        return request.post(
                env.getApiBaseURL() + "/auth/login",
                withHeaders().setData(
                        Map.of(
                                "username", username,
                                "password", password
                        )
                )
        );
    }
}
