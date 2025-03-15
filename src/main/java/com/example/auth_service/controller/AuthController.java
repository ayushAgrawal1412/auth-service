package com.example.auth_service.controller;

import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> signup(@RequestBody AuthRequest request) {
        return authService.signup(request)
                .map(response -> {
                    if ("Rate limit exceeded. Try again later.".equals(response)) {
                        return ResponseEntity.status(429).body(response);
                    }
                    return ResponseEntity.ok(response);
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody AuthRequest request) {
        return authService.login(request)
                .map(response -> {
                    if ("Rate limit exceeded. Try again later.".equals(response)) {
                        return ResponseEntity.status(429).body(response);
                    }
                    return ResponseEntity.ok(response);
                });
    }
}
