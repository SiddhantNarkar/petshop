package com.petshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.config.JwtAuthFilter;
import com.petshop.config.JwtUtil;
import com.petshop.config.SecurityConfig;
import com.petshop.dto.ServiceDTO;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.service.ServiceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiV1ServicesController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class, JwtUtil.class})
class ApiV1ServicesControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtUtil jwtUtil;

    // ✅ FIX: Mock ServiceManager (what the controller depends on),
    // not the individual repositories (GroomingServiceRepository, etc.).
    @MockitoBean ServiceManager serviceManager;

    private final ObjectMapper mapper = new ObjectMapper();
    private String adminToken;
    private ServiceDTO sampleGroomingDTO;
    private ServiceDTO sampleVaccinationDTO;

    @BeforeEach
    void setUp() {
        adminToken = "Bearer " + jwtUtil.generateToken("tejas", "ROLE_ADMIN");

        sampleGroomingDTO = new ServiceDTO(1, "Full Groom", "Complete grooming package", 500.0, true);
        sampleVaccinationDTO = new ServiceDTO(1, "Rabies Vaccine", "Annual rabies vaccination", 300.0, true);
    }

    // ── Grooming GET ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/services/grooming returns all grooming services")
    void getAllGrooming_returns200() throws Exception {
        when(serviceManager.getAllGrooming()).thenReturn(List.of(sampleGroomingDTO));

        mockMvc.perform(get("/api/v1/services/grooming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Full Groom"))
                .andExpect(jsonPath("$[0].price").value(500.0));
    }

    @Test
    @DisplayName("GET /api/v1/services/pets/{petId}/grooming returns pet grooming services")
    void getPetGrooming_found_returns200() throws Exception {
        when(serviceManager.getPetGroomingServices(1)).thenReturn(List.of(sampleGroomingDTO));

        mockMvc.perform(get("/api/v1/services/pets/1/grooming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Full Groom"));
    }

    @Test
    @DisplayName("GET /api/v1/services/pets/{petId}/grooming returns 404 for unknown pet")
    void getPetGrooming_petNotFound_returns404() throws Exception {
        when(serviceManager.getPetGroomingServices(99))
                .thenThrow(new ResourceNotFoundException("Pet not found"));

        mockMvc.perform(get("/api/v1/services/pets/99/grooming"))
                .andExpect(status().isNotFound());
    }

    // ── Grooming POST ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/services/grooming returns 403 without token")
    void createGrooming_noToken_returns403() throws Exception {
        mockMvc.perform(post("/api/v1/services/grooming")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Bath"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/services/grooming creates service with admin token")
    void createGrooming_withToken_returns200() throws Exception {
        when(serviceManager.createGrooming(any())).thenReturn(sampleGroomingDTO);

        mockMvc.perform(post("/api/v1/services/grooming")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "name", "Full Groom",
                                "description", "Complete grooming package",
                                "price", 500.0,
                                "available", true
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Full Groom"));
    }

    @Test
    @DisplayName("POST /api/v1/services/pets/{petId}/grooming/{serviceId} assigns grooming with admin token")
    void assignGrooming_withToken_returns200() throws Exception {
        when(serviceManager.assignGrooming(1, 1)).thenReturn(Map.of("message", "Grooming service assigned"));

        mockMvc.perform(post("/api/v1/services/pets/1/grooming/1")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Grooming service assigned"));
    }

    @Test
    @DisplayName("POST /api/v1/services/pets/{petId}/grooming/{serviceId} returns 403 without token")
    void assignGrooming_noToken_returns403() throws Exception {
        mockMvc.perform(post("/api/v1/services/pets/1/grooming/1"))
                .andExpect(status().isForbidden());
    }

    // ── Vaccination GET ───────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/services/vaccinations returns all vaccinations")
    void getAllVaccinations_returns200() throws Exception {
        when(serviceManager.getAllVaccinations()).thenReturn(List.of(sampleVaccinationDTO));

        mockMvc.perform(get("/api/v1/services/vaccinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rabies Vaccine"));
    }

    @Test
    @DisplayName("GET /api/v1/services/pets/{petId}/vaccinations returns pet vaccinations")
    void getPetVaccinations_found_returns200() throws Exception {
        when(serviceManager.getPetVaccinations(1)).thenReturn(List.of(sampleVaccinationDTO));

        mockMvc.perform(get("/api/v1/services/pets/1/vaccinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rabies Vaccine"));
    }

    // ── Vaccination POST ──────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/services/vaccinations creates vaccination with admin token")
    void createVaccination_withToken_returns200() throws Exception {
        when(serviceManager.createVaccination(any())).thenReturn(sampleVaccinationDTO);

        mockMvc.perform(post("/api/v1/services/vaccinations")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "name", "Rabies Vaccine",
                                "description", "Annual rabies vaccination",
                                "price", 300.0,
                                "available", true
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rabies Vaccine"));
    }

    @Test
    @DisplayName("POST /api/v1/services/vaccinations returns 403 without token")
    void createVaccination_noToken_returns403() throws Exception {
        mockMvc.perform(post("/api/v1/services/vaccinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Rabies"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/services/pets/{petId}/vaccinations/{vacId} assigns vaccination")
    void assignVaccination_withToken_returns200() throws Exception {
        when(serviceManager.assignVaccination(1, 1)).thenReturn(Map.of("message", "Vaccination assigned"));

        mockMvc.perform(post("/api/v1/services/pets/1/vaccinations/1")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vaccination assigned"));
    }
}