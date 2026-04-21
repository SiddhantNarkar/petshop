package com.petshop.controller;

import com.petshop.entity.Employee;
import com.petshop.entity.PetFood;
import com.petshop.entity.Supplier;
import com.petshop.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
public class ApiV1InventoryController {

    private final InventoryService inventoryService;

    public ApiV1InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // ===== FOOD =====

    @GetMapping("/food")
    public List<PetFood> getAllFood() {
        return inventoryService.getAllFood();
    }

    @GetMapping("/food/{id}")
    public PetFood getFoodById(@PathVariable int id) {
        return inventoryService.getFoodById(id);
    }

    @PostMapping("/food")
    public PetFood createFood(@RequestBody Map<String, Object> payload) {
        return inventoryService.createFood(payload);
    }

    @PutMapping("/food/{id}")
    public PetFood updateFood(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        return inventoryService.updateFood(id, payload);
    }

    // ===== SUPPLIER =====

    @GetMapping("/suppliers")
    public List<Map<String, Object>> getAllSuppliers() {
        return inventoryService.getAllSuppliers();
    }

    @GetMapping("/suppliers/{id}")
    public Supplier getSupplierById(@PathVariable int id) {
        return inventoryService.getSupplierById(id);
    }

    @PostMapping("/suppliers")
    public Supplier createSupplier(@RequestBody Map<String, Object> payload) {
        return inventoryService.createSupplier(payload);
    }

    @PutMapping("/suppliers/{id}")
    public Supplier updateSupplier(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        return inventoryService.updateSupplier(id, payload);
    }

    // ===== EMPLOYEE =====

    @GetMapping("/employees")
    public List<Map<String, Object>> getAllEmployees() {
        return inventoryService.getAllEmployees();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return inventoryService.getEmployeeById(id);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Map<String, Object> payload) {
        return inventoryService.createEmployee(payload);
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        return inventoryService.updateEmployee(id, payload);
    }

    // ===== ASSIGN =====

    @PostMapping("/pets/{petId}/food/{foodId}")
    public Map<String, Object> assignFood(@PathVariable int petId, @PathVariable int foodId) {
        return inventoryService.assignFood(petId, foodId);
    }

    @PostMapping("/pets/{petId}/suppliers/{supplierId}")
    public Map<String, Object> assignSupplier(@PathVariable int petId, @PathVariable int supplierId) {
        return inventoryService.assignSupplier(petId, supplierId);
    }

    @PostMapping("/pets/{petId}/employees/{employeeId}")
    public Map<String, Object> assignEmployee(@PathVariable int petId, @PathVariable int employeeId) {
        return inventoryService.assignEmployee(petId, employeeId);
    }

    @GetMapping("/pets/{petId}/food")
    public List<PetFood> getPetFoods(@PathVariable int petId) {
        return inventoryService.getPetFoods(petId);
    }

    @GetMapping("/pets/{petId}/suppliers")
    public List<Supplier> getPetSuppliers(@PathVariable int petId) {
        return inventoryService.getPetSuppliers(petId);
    }

    @GetMapping("/pets/{petId}/employees")
    public List<Employee> getPetEmployees(@PathVariable int petId) {
        return inventoryService.getPetEmployees(petId);
    }
}
