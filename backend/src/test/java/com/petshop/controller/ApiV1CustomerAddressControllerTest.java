package com.petshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.config.JwtAuthFilter;
import com.petshop.config.JwtUtil;
import com.petshop.config.SecurityConfig;
import com.petshop.dto.AddressDTO;
import com.petshop.dto.CustomerDTO;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiV1CustomerAddressController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class, JwtUtil.class})
class ApiV1CustomerAddressControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtUtil jwtUtil;


    @MockitoBean CustomerService customerService;

    private final ObjectMapper mapper = new ObjectMapper();
    private String adminToken;
    private CustomerDTO sampleCustomerDTO;
    private AddressDTO sampleAddressDTO;

    @BeforeEach
    void setUp() {
        adminToken = "Bearer " + jwtUtil.generateToken("sharvari", "ROLE_ADMIN");

        sampleAddressDTO = new AddressDTO(1, "123 Main St", "Mumbai", "MH", "400001");

        sampleCustomerDTO = new CustomerDTO(
                1, "John", "Doe", "john@example.com", "9876543210", sampleAddressDTO
        );
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/customers/{id} returns customer when found")
    void getCustomer_found_returns200() throws Exception {
        when(customerService.getById(1)).thenReturn(sampleCustomerDTO);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} returns 404 when not found")
    void getCustomer_notFound_returns404() throws Exception {
        when(customerService.getById(99))
                .thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id}/address returns address when present")
    void getCustomerAddress_found_returns200() throws Exception {
        when(customerService.getAddress(1)).thenReturn(sampleAddressDTO);

        mockMvc.perform(get("/api/v1/customers/1/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Mumbai"));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id}/address returns 404 when no address")
    void getCustomerAddress_noAddress_returns404() throws Exception {
        when(customerService.getAddress(1))
                .thenThrow(new ResourceNotFoundException("Address not found"));

        mockMvc.perform(get("/api/v1/customers/1/address"))
                .andExpect(status().isNotFound());
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/customers/register requires admin token")
    void register_noToken_returns403() throws Exception {
        mockMvc.perform(post("/api/v1/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("firstName", "Jane"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/customers/register with admin token creates customer")
    void register_withToken_returns200() throws Exception {
        when(customerService.register(any())).thenReturn(sampleCustomerDTO);

        mockMvc.perform(post("/api/v1/customers/register")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "firstName", "John",
                                "lastName", "Doe",
                                "email", "john@example.com",
                                "phone", "9876543210"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("POST /api/v1/customers/login with admin token returns customer on valid email")
    void login_validEmail_returns200() throws Exception {
        // POST /api/** requires ROLE_ADMIN per SecurityConfig — must send admin token
        when(customerService.login("john@example.com", null)).thenReturn(Map.of(
                "message", "Login successful",
                "customerId", 1,
                "customer", sampleCustomerDTO
        ));

        mockMvc.perform(post("/api/v1/customers/login")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("email", "john@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    @DisplayName("POST /api/v1/customers/login with admin token returns 404 on unknown email")
    void login_unknownEmail_returns404() throws Exception {
        // ResourceNotFoundException → GlobalExceptionHandler → 404 NOT_FOUND
        when(customerService.login("unknown@example.com", null))
                .thenThrow(new ResourceNotFoundException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/customers/login")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("email", "unknown@example.com"))))
                .andExpect(status().isNotFound());
    }

    // ── PUT ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/customers/{id} returns 403 without token")
    void updateCustomer_noToken_returns403() throws Exception {
        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("firstName", "Updated"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/v1/customers/{id} updates customer with admin token")
    void updateCustomer_withToken_returns200() throws Exception {
        when(customerService.update(eq(1), any())).thenReturn(sampleCustomerDTO);

        mockMvc.perform(put("/api/v1/customers/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("firstName", "UpdatedName"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/customers/addresses/{id} returns 403 without token")
    void updateAddress_noToken_returns403() throws Exception {
        mockMvc.perform(put("/api/v1/customers/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("city", "Pune"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/v1/customers/addresses/{id} updates address with admin token")
    void updateAddress_withToken_returns200() throws Exception {
        when(customerService.updateAddressById(eq(1), any())).thenReturn(sampleAddressDTO);

        mockMvc.perform(put("/api/v1/customers/addresses/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("city", "Pune"))))
                .andExpect(status().isOk());
    }
}