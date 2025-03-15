package com.example.auth_service.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RateLimiterClient {

    private final WebClient webClient;

    public RateLimiterClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://51.21.239.22:8080").build();
    }

    public Mono<Boolean> isRequestAllowed(String clientID) {
        return webClient.get()
                .uri("/api/request?clientID=" + clientID)
                .retrieve()
                .bodyToMono(String.class) // Receive as String first
                .map(response -> {
                    System.out.println("Raw response from rate limiter: " + response);
                    if (response.contains("allowed")) { // Check for success message
                        System.out.println("Request allowed for clientID: " + clientID);
                        return true;
                    } else if (response.contains("denied")) { // Check for rate limit exceeded message
                        System.out.println("Rate limit exceeded for clientID: " + clientID);
                        return false;
                    }
                    return false; // Fallback in case of unknown response
                })
                .onErrorResume(ex -> {
                    System.out.println("Exception occurred while checking rate limit: " + ex.getMessage());
                    return Mono.just(false);
                });
    }
}
