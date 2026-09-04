package com.example.framework.api;

import com.example.framework.config.ApiSecrets;
import com.example.framework.config.Environment;
import com.microsoft.playwright.APIResponse;

import java.util.Map;

public class UserClient extends BaseApiClient {
    public UserClient(Environment env, ApiSecrets apiSecrets) {
        super(env, apiSecrets);
    }

    public APIResponse getAllUsers() {
        return request.get(
                env.getApiBaseURL() + "/users",
                withHeaders()
        );
    }


    public APIResponse getUserById(String userId) {
        return request.get(
                env.getApiBaseURL() + "/users/" + userId,
                withHeaders()
        );
    }

    public APIResponse createUser(String username, String email) {
        return request.post(
                env.getApiBaseURL() + "/users",
                withHeaders().setData(
                        Map.of(
                                "username", username,
                                "email", email
                        )
                )
        );
    }

    public APIResponse deleteUser(String userId) {
        return request.delete(
                env.getApiBaseURL() + "/users/" + userId,
                withHeaders()
        );
    }
}
