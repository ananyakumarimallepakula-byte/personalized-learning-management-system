package com.personalizedlearning.controller;

import com.personalizedlearning.dto.LoginRequest;
import com.personalizedlearning.dto.LoginResponse;
import com.personalizedlearning.dto.RegisterRequest;
import com.personalizedlearning.dto.UserDto;
import com.personalizedlearning.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
        UserDto registered = authService.register(request);
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/{userId}")
    public ResponseEntity<UserDto> getCurrentUser(@PathVariable Long userId) {
        UserDto user = authService.getCurrentUser(userId);
        return ResponseEntity.ok(user);
    }
}
