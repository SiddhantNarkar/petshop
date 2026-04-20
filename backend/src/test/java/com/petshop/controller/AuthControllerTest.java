package com.petshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.config.JwtAuthFilter;
import com.petshop.config.JwtUtil;
import com.petshop.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class, JwtUtil.class})
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtUtil jwtUtil;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("POST /api/auth/login with valid admin credentials returns token")
    void login_validAdmin_returnsToken() throws Exception {
        Map<String, String> body = Map.of(
                "username", "sharvari",
                "password", "password123",
                "role", "ROLE_ADMIN"
        );
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("POST /api/auth/login with wrong admin password returns 401")
    void login_wrongAdminPassword_returns401() throws Exception {
        Map<String, String> body = Map.of(
                "username", "sharvari",
                "password", "wrongpassword",
                "role", "ROLE_ADMIN"
        );
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/login with unknown user as admin returns 401")
    void login_unknownUserAsAdmin_returns401() throws Exception {
        Map<String, String> body = Map.of(
                "username", "hacker",
                "password", "password123",
                "role", "ROLE_ADMIN"
        );
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/login with ROLE_USER returns user token")
    void login_roleUser_returnsUserToken() throws Exception {
        Map<String, String> body = Map.of(
                "username", "regularuser",
                "password", "anypassword",
                "role", "ROLE_USER"
        );
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    @DisplayName("POST /api/auth/login with blank username returns 400")
    void login_blankUsername_returns400() throws Exception {
        Map<String, String> body = Map.of(
                "username", "",
                "password", "password123",
                "role", "ROLE_USER"
        );
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
