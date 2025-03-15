package com.example.auth_service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class RateLimiterClient {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public RateLimiterClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Boolean> isRequestAllowed(String clientID) {
        return webClientBuilder.build()
                .get()
                .uri("http://13.49.80.120:8080/api/request?clientID=" + clientID) // Use service discovery instead of hardcoding IP
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if ("Request denied due to rate limit exceeded".equals(response)) {
                        System.out.println("Rate limit exceeded");
                        return false;
                    }
                    System.out.println("Request allowed");
                    return true;
                })
                .onErrorResume(ex -> {
                    System.out.println("Exception occurred: " + ex.getMessage());
                    return Mono.just(false);
                });
    }
}
