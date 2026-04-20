package com.petshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.config.JwtAuthFilter;
import com.petshop.config.JwtUtil;
import com.petshop.config.SecurityConfig;
import com.petshop.dto.TransactionDTO;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiV1TransactionsController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class, JwtUtil.class})
class ApiV1TransactionsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtUtil jwtUtil;

    @MockitoBean TransactionService transactionService;

    private final ObjectMapper mapper = new ObjectMapper();
    private String adminToken;
    private TransactionDTO sampleDTO;

    @BeforeEach
    void setUp() {
        adminToken = "Bearer " + jwtUtil.generateToken("siddhant", "ROLE_ADMIN");

        sampleDTO = new TransactionDTO(
                1, 10, 5,
                LocalDate.now(),
                5000.0,
                "Success"
        );
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/transactions returns all transactions")
    void getAllTransactions_returns200() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(List.of(sampleDTO));

        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].customerId").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} returns transaction when found")
    void getTransactionById_found_returns200() throws Exception {
        when(transactionService.getTransactionById(1)).thenReturn(sampleDTO);

        mockMvc.perform(get("/api/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(5000.0));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/{id} returns 404 when not found")
    void getTransactionById_notFound_returns404() throws Exception {
        when(transactionService.getTransactionById(99))
                .thenThrow(new ResourceNotFoundException("Transaction not found"));

        mockMvc.perform(get("/api/v1/transactions/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/transactions/orders/{id} returns transaction")
    void getOrderById_returns200() throws Exception {
        when(transactionService.getTransactionById(1)).thenReturn(sampleDTO);

        mockMvc.perform(get("/api/v1/transactions/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(5));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/customers/{id}/orders returns customer orders")
    void getOrdersByCustomer_returns200() throws Exception {
        when(transactionService.getOrdersByCustomer(10)).thenReturn(List.of(sampleDTO));

        mockMvc.perform(get("/api/v1/transactions/customers/10/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/transactions/customers/{id} returns summary")
    void getCustomerSummary_returns200() throws Exception {
        when(transactionService.getCustomerSummary(10)).thenReturn(Map.of(
                "customerId", 10,
                "totalTransactions", 1,
                "transactions", List.of(sampleDTO)
        ));

        mockMvc.perform(get("/api/v1/transactions/customers/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(10))
                .andExpect(jsonPath("$.totalTransactions").value(1));
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/transactions/orders returns 403 without token")
    void createOrder_noToken_returns403() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("customerId", 1, "petId", 1, "amount", 500))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/transactions/orders with admin token creates order with Success status")
    void createOrder_lowAmount_returnsSuccessStatus() throws Exception {
        when(transactionService.createOrder(any())).thenReturn(sampleDTO);

        mockMvc.perform(post("/api/v1/transactions/orders")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "customerId", 10, "petId", 5, "amount", 5000
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"));
    }

    @Test
    @DisplayName("POST /api/v1/transactions/orders with amount >= 10000 results in Failed status")
    void createOrder_highAmount_returnsFailedStatus() throws Exception {
        TransactionDTO failedDTO = new TransactionDTO(2, 10, 5, LocalDate.now(), 15000.0, "Failed");
        when(transactionService.createOrder(any())).thenReturn(failedDTO);

        mockMvc.perform(post("/api/v1/transactions/orders")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "customerId", 10, "petId", 5, "amount", 15000
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Failed"));
    }

    // ── PUT ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/transactions/{id}/status returns 403 without token")
    void updateStatus_noToken_returns403() throws Exception {
        mockMvc.perform(put("/api/v1/transactions/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("status", "Failed"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/v1/transactions/{id}/status updates status with admin token")
    void updateStatus_withToken_returns200() throws Exception {
        TransactionDTO updated = new TransactionDTO(1, 10, 5, LocalDate.now(), 5000.0, "Failed");
        when(transactionService.updateStatus(eq(1), any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/transactions/1/status")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("status", "Failed"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Failed"));
    }
}