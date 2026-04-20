package com.petshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.config.JwtAuthFilter;
import com.petshop.config.JwtUtil;
import com.petshop.config.SecurityConfig;
import com.petshop.entity.Employee;
import com.petshop.entity.Pet;
import com.petshop.entity.PetFood;
import com.petshop.entity.Supplier;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiV1InventoryController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class, JwtUtil.class})
class ApiV1InventoryControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtUtil jwtUtil;

    @MockitoBean InventoryService inventoryService;

    private final ObjectMapper mapper = new ObjectMapper();
    private String adminToken;
    private PetFood sampleFood;
    private Supplier sampleSupplier;
    private Employee sampleEmployee;
    private Pet samplePet;

    @BeforeEach
    void setUp() {
        adminToken = "Bearer " + jwtUtil.generateToken("yatesh", "ROLE_ADMIN");

        sampleFood = new PetFood();
        sampleFood.setFoodId(1);
        sampleFood.setName("Premium Kibble");
        sampleFood.setBrand("Royal Canin");
        sampleFood.setType("Dry");
        sampleFood.setQuantity(100);
        sampleFood.setPrice(999.0);

        sampleSupplier = new Supplier();
        sampleSupplier.setSupplierId(1);
        sampleSupplier.setName("PetCare Suppliers");
        sampleSupplier.setContactPerson("John Doe");
        sampleSupplier.setPhoneNumber("9876543210");
        sampleSupplier.setEmail("petcare@gmail.com");

        sampleEmployee = new Employee();
        sampleEmployee.setEmployeeId(1);
        sampleEmployee.setFirstName("Alice");
        sampleEmployee.setLastName("Smith");
        sampleEmployee.setPosition("Groomer");
        sampleEmployee.setPhoneNumber("9876543210");

        samplePet = new Pet();
        samplePet.setPetId(1);
        samplePet.setName("Buddy");
        samplePet.setFoods(new ArrayList<>());
        samplePet.setSuppliers(new ArrayList<>());
        samplePet.setEmployees(new ArrayList<>());
    }

    // ── Food GET ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/inventory/food returns all food items")
    void getAllFood_returns200() throws Exception {
        when(inventoryService.getAllFood()).thenReturn(List.of(sampleFood));

        mockMvc.perform(get("/api/v1/inventory/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Premium Kibble"));
    }

    @Test
    @DisplayName("GET /api/v1/inventory/food/{id} returns food when found")
    void getFoodById_found_returns200() throws Exception {
        when(inventoryService.getFoodById(1)).thenReturn(sampleFood);

        mockMvc.perform(get("/api/v1/inventory/food/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Premium Kibble"));
    }

    @Test
    @DisplayName("GET /api/v1/inventory/food/{id} returns 404 when not found")
    void getFoodById_notFound_returns404() throws Exception {
        when(inventoryService.getFoodById(99))
                .thenThrow(new ResourceNotFoundException("Food not found"));

        mockMvc.perform(get("/api/v1/inventory/food/99"))
                .andExpect(status().isNotFound());
    }

    // ── Food POST/PUT ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/inventory/food returns 403 without token")
    void createFood_noToken_returns403() throws Exception {
        mockMvc.perform(post("/api/v1/inventory/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Kibble"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/inventory/food creates food with admin token")
    void createFood_withToken_returns200() throws Exception {
        when(inventoryService.createFood(any())).thenReturn(sampleFood);

        mockMvc.perform(post("/api/v1/inventory/food")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "name", "Premium Kibble", "brand", "Royal Canin",
                                "type", "Dry", "quantity", 100, "price", 999.0
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Premium Kibble"));
    }

    @Test
    @DisplayName("PUT /api/v1/inventory/food/{id} returns 403 without token")
    void updateFood_noToken_returns403() throws Exception {
        mockMvc.perform(put("/api/v1/inventory/food/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Updated"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/v1/inventory/food/{id} updates food with admin token")
    void updateFood_withToken_returns200() throws Exception {
        when(inventoryService.updateFood(eq(1), any())).thenReturn(sampleFood);

        mockMvc.perform(put("/api/v1/inventory/food/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Updated Kibble"))))
                .andExpect(status().isOk());
    }

    // ── Supplier GET ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/inventory/suppliers/{id} returns supplier when found")
    void getSupplierById_found_returns200() throws Exception {
        when(inventoryService.getSupplierById(1)).thenReturn(sampleSupplier);

        mockMvc.perform(get("/api/v1/inventory/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PetCare Suppliers"));
    }

    // ── Supplier POST/PUT ─────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/inventory/suppliers creates supplier with admin token")
    void createSupplier_withToken_returns200() throws Exception {
        when(inventoryService.createSupplier(any())).thenReturn(sampleSupplier);

        mockMvc.perform(post("/api/v1/inventory/suppliers")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "name", "PetCare Suppliers",
                                "contactPerson", "John Doe",
                                "phoneNumber", "9876543210",
                                "email", "petcare@gmail.com"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PetCare Suppliers"));
    }

    @Test
    @DisplayName("PUT /api/v1/inventory/suppliers/{id} updates supplier with admin token")
    void updateSupplier_withToken_returns200() throws Exception {
        when(inventoryService.updateSupplier(eq(1), any())).thenReturn(sampleSupplier);

        mockMvc.perform(put("/api/v1/inventory/suppliers/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Updated Supplier"))))
                .andExpect(status().isOk());
    }

    // ── Employee GET ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/inventory/employees/{id} returns employee when found")
    void getEmployeeById_found_returns200() throws Exception {
        when(inventoryService.getEmployeeById(1)).thenReturn(sampleEmployee);

        mockMvc.perform(get("/api/v1/inventory/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/v1/inventory/employees/{id} returns 404 when not found")
    void getEmployeeById_notFound_returns404() throws Exception {
        when(inventoryService.getEmployeeById(99))
                .thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/v1/inventory/employees/99"))
                .andExpect(status().isNotFound());
    }

    // ── Employee POST/PUT ─────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/inventory/employees creates employee with admin token")
    void createEmployee_withToken_returns200() throws Exception {
        when(inventoryService.createEmployee(any())).thenReturn(sampleEmployee);

        mockMvc.perform(post("/api/v1/inventory/employees")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "firstName", "Alice", "lastName", "Smith",
                                "position", "Groomer", "phoneNumber", "9876543210"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    @DisplayName("PUT /api/v1/inventory/employees/{id} updates employee with admin token")
    void updateEmployee_withToken_returns200() throws Exception {
        when(inventoryService.updateEmployee(eq(1), any())).thenReturn(sampleEmployee);

        mockMvc.perform(put("/api/v1/inventory/employees/1")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("position", "Senior Groomer"))))
                .andExpect(status().isOk());
    }

    // ── Pet assignment POST ───────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/inventory/pets/{petId}/food/{foodId} assigns food to pet")
    void assignFood_withToken_returns200() throws Exception {
        when(inventoryService.assignFood(1, 1)).thenReturn(Map.of("message", "Food assigned"));

        mockMvc.perform(post("/api/v1/inventory/pets/1/food/1")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Food assigned"));
    }

    @Test
    @DisplayName("POST /api/v1/inventory/pets/{petId}/suppliers/{supplierId} assigns supplier")
    void assignSupplier_withToken_returns200() throws Exception {
        when(inventoryService.assignSupplier(1, 1)).thenReturn(Map.of("message", "Supplier assigned"));

        mockMvc.perform(post("/api/v1/inventory/pets/1/suppliers/1")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Supplier assigned"));
    }

    @Test
    @DisplayName("POST /api/v1/inventory/pets/{petId}/employees/{employeeId} assigns employee")
    void assignEmployee_withToken_returns200() throws Exception {
        when(inventoryService.assignEmployee(1, 1)).thenReturn(Map.of("message", "Employee assigned"));

        mockMvc.perform(post("/api/v1/inventory/pets/1/employees/1")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee assigned"));
    }

    // ── Pet relationship GET ──────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/inventory/pets/{petId}/food returns pet food list")
    void getPetFoods_returns200() throws Exception {
        when(inventoryService.getPetFoods(1)).thenReturn(List.of(sampleFood));

        mockMvc.perform(get("/api/v1/inventory/pets/1/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Premium Kibble"));
    }

    @Test
    @DisplayName("GET /api/v1/inventory/pets/{petId}/suppliers returns pet suppliers")
    void getPetSuppliers_returns200() throws Exception {
        when(inventoryService.getPetSuppliers(1)).thenReturn(List.of(sampleSupplier));

        mockMvc.perform(get("/api/v1/inventory/pets/1/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("PetCare Suppliers"));
    }

    @Test
    @DisplayName("GET /api/v1/inventory/pets/{petId}/employees returns pet employees")
    void getPetEmployees_returns200() throws Exception {
        when(inventoryService.getPetEmployees(1)).thenReturn(List.of(sampleEmployee));

        mockMvc.perform(get("/api/v1/inventory/pets/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }
}