package com.example.auth_service.controller;

import com.example.auth_service.dto.AuthRequestDTO;
import com.example.auth_service.dto.UserResponseDTO;
import com.example.auth_service.model.User;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequestDTO request) {
        Object result = authService.signup(request);

        if (result instanceof String message) {
            if ("Rate limit exceeded. Try again later.".equals(message)) {
                return ResponseEntity.status(429).body(message);
            } else {
                return ResponseEntity.badRequest().body(message);
            }
        }

        UserResponseDTO createdUser = (UserResponseDTO) result;
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDTO request) {
        String response = authService.login(request);

        if ("Rate limit exceeded. Try again later.".equals(response)) {
            return ResponseEntity.status(429).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
