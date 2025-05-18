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

    public boolean isRequestAllowed(String clientID) {
        try {
            String response = webClient.get()
                    .uri("/api/request?clientID=" + clientID)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Raw response from rate limiter: " + response);

            if (response != null && response.contains("allowed")) {
                System.out.println("Request allowed for clientID: " + clientID);
                return true;
            } else if (response != null && response.contains("denied")) {
                System.out.println("Rate limit exceeded for clientID: " + clientID);
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred while checking rate limit: " + ex.getMessage());
        }

        return false;
    }
}
