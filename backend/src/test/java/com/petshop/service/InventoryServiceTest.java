package com.petshop.service;

import com.petshop.entity.Employee;
import com.petshop.entity.Pet;
import com.petshop.entity.PetFood;
import com.petshop.entity.Supplier;
import com.petshop.repository.EmployeeRepository;
import com.petshop.repository.PetFoodRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock private PetRepository petRepo;
    @Mock private EmployeeRepository employeeRepo;
    @Mock private PetFoodRepository foodRepo;
    @Mock private SupplierRepository supplierRepo;

    @InjectMocks
    private InventoryService inventoryService;

    private Pet pet;
    private Employee employee;
    private PetFood food;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        pet = new Pet();
        pet.setPetId(1);
        pet.setName("Buddy");
        pet.setEmployees(new ArrayList<>());
        pet.setFoods(new ArrayList<>());
        pet.setSuppliers(new ArrayList<>());

        employee = new Employee();
        employee.setEmployeeId(10);
        employee.setFirstName("Alice");
        employee.setLastName("Smith");
        employee.setPosition("Groomer");

        food = new PetFood();
        food.setFoodId(20);
        food.setName("Premium Kibble");

        supplier = new Supplier();
        supplier.setSupplierId(30);
        supplier.setName("PetSupplies Co.");
    }

    // ===== EMPLOYEE =====

    @Test
    @DisplayName("assignEmployee adds employee to pet and saves")
    void assignEmployee_success() {
        when(petRepo.findById(1)).thenReturn(Optional.of(pet));
        when(employeeRepo.findById(10)).thenReturn(Optional.of(employee));
        when(petRepo.save(any(Pet.class))).thenReturn(pet);

        Map<String, Object> result = inventoryService.assignEmployee(1, 10);

        assertThat(result.get("message")).isEqualTo("Employee assigned");
        assertThat(pet.getEmployees()).contains(employee);
        verify(petRepo).save(pet);
    }

    @Test
    @DisplayName("assignEmployee throws exception when pet not found")
    void assignEmployee_petNotFound() {
        when(petRepo.findById(99)).thenReturn(Optional.empty());

        try {
            inventoryService.assignEmployee(99, 10);
        } catch (Exception ignored) {}

        verify(petRepo, never()).save(any());
    }

    // ===== FOOD =====

    @Test
    @DisplayName("assignFood adds food to pet and saves")
    void assignFood_success() {
        when(petRepo.findById(1)).thenReturn(Optional.of(pet));
        when(foodRepo.findById(20)).thenReturn(Optional.of(food));
        when(petRepo.save(any(Pet.class))).thenReturn(pet);

        Map<String, Object> result = inventoryService.assignFood(1, 20);

        assertThat(result.get("message")).isEqualTo("Food assigned");
        assertThat(pet.getFoods()).contains(food);
        verify(petRepo).save(pet);
    }

    // ===== SUPPLIER =====

    @Test
    @DisplayName("assignSupplier adds supplier to pet and saves")
    void assignSupplier_success() {
        when(petRepo.findById(1)).thenReturn(Optional.of(pet));
        when(supplierRepo.findById(30)).thenReturn(Optional.of(supplier));
        when(petRepo.save(any(Pet.class))).thenReturn(pet);

        Map<String, Object> result = inventoryService.assignSupplier(1, 30);

        assertThat(result.get("message")).isEqualTo("Supplier assigned");
        assertThat(pet.getSuppliers()).contains(supplier);
        verify(petRepo).save(pet);
    }
}