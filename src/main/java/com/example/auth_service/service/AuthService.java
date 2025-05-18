package com.example.auth_service.service;

import com.example.auth_service.dto.AuthRequestDTO;
import com.example.auth_service.dto.UserResponseDTO;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.utils.RateLimiterClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final RateLimiterClient rateLimiterClient;
    private final UserRepository userRepository;

    public AuthService(RateLimiterClient rateLimiterClient, UserRepository userRepository) {
        this.rateLimiterClient = rateLimiterClient;
        this.userRepository = userRepository;
    }

    public Object signup(AuthRequestDTO request) {
        boolean isAllowed = rateLimiterClient.isRequestAllowed(request.getUsername());

        if (!isAllowed) {
            return "Rate limit exceeded. Try again later.";
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "User already exists!";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername());
    }

    public String login(AuthRequestDTO request) {
        boolean isAllowed = rateLimiterClient.isRequestAllowed(request.getUsername());

        if (!isAllowed) {
            return "Rate limit exceeded. Try again later.";
        }

        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                return "Login successful!";
            } else {
                return "Invalid username or password!";
            }
        } else {
            return "Invalid username or password!";
        }
    }
}
