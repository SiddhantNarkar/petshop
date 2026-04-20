package com.petshop.controller;

import com.petshop.config.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final String adminPassword;
    private final Set<String> adminUsers;

    public AuthController(
            JwtUtil jwtUtil,
            @Value("${app.admin.password}") String adminPassword,
            @Value("${app.admin.users}") String adminUsersRaw) {
        this.jwtUtil = jwtUtil;
        this.adminPassword = adminPassword;
        this.adminUsers = Arrays.stream(adminUsersRaw.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username      = body.getOrDefault("username", "").trim();
        String password      = body.getOrDefault("password", "").trim();
        String requestedRole = body.getOrDefault("role", "ROLE_USER");

        if ("ROLE_ADMIN".equals(requestedRole)) {
            if (adminUsers.contains(username) && adminPassword.equals(password)) {
                String token = jwtUtil.generateToken(username, "ROLE_ADMIN");
                return ResponseEntity.ok(Map.of("token", token, "role", "ROLE_ADMIN"));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid admin credentials"));
        }

        if (!username.isBlank()) {
            String token = jwtUtil.generateToken(username, "ROLE_USER");
            return ResponseEntity.ok(Map.of("token", token, "role", "ROLE_USER"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Username is required"));
    }
}