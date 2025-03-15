package com.example.auth_service.service;

import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.utils.RateLimiterClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final RateLimiterClient rateLimiterClient;
    private final Map<String, String> users = new HashMap<>();

    public AuthService(RateLimiterClient rateLimiterClient) {
        this.rateLimiterClient = rateLimiterClient;
    }

    public Mono<String> signup(AuthRequest request) {
        return rateLimiterClient.isRequestAllowed(request.getUsername())
                .flatMap(isAllowed -> {
                    if (!Boolean.TRUE.equals(isAllowed)) {
                        return Mono.just("Rate limit exceeded. Try again later.");
                    }

                    if (users.containsKey(request.getUsername())) {
                        return Mono.just("User already exists!");
                    }

                    users.put(request.getUsername(), request.getPassword());
                    return Mono.just("User registered successfully!");
                });
    }

    public Mono<String> login(AuthRequest request) {
        return rateLimiterClient.isRequestAllowed(request.getUsername())
                .flatMap(isAllowed -> {
                    if (!Boolean.TRUE.equals(isAllowed)) {
                        return Mono.just("Rate limit exceeded. Try again later.");
                    }

                    String password = users.get(request.getUsername());
                    if (password != null && password.equals(request.getPassword())) {
                        return Mono.just("Login successful!");
                    } else {
                        return Mono.just("Invalid username or password!");
                    }
                });
    }
}
