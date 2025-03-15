package com.example.auth_service.service;

import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.utils.RateLimiterClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final RateLimiterClient rateLimiterClient;
    private final Map<String, String> users = new HashMap<>();

    public AuthService(RateLimiterClient rateLimiterClient) {
        this.rateLimiterClient = rateLimiterClient;
    }

    public String signup(AuthRequest request) {
        // Rate limiting check
        boolean isAllowed = Boolean.TRUE.equals(rateLimiterClient.isRequestAllowed(request.getUsername())
                .block()); // Blocking to get the result synchronously

        if (!isAllowed) {
            return "Rate limit exceeded. Try again later.";
        }

        if (users.containsKey(request.getUsername())) {
            return "User already exists!";
        }

        users.put(request.getUsername(), request.getPassword());
        return "User registered successfully!";
    }

    public String login(AuthRequest request) {
        boolean isAllowed = Boolean.TRUE.equals(rateLimiterClient.isRequestAllowed(request.getUsername())
                .block()); // Blocking to get the result synchronously

        if (!isAllowed) {
            return "Rate limit exceeded. Try again later.";
        }

        String password = users.get(request.getUsername());
        if (password != null && password.equals(request.getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid username or password!";
        }
    }
}
