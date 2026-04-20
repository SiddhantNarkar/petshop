package com.petshop.service;


import com.petshop.dto.EmployeeDTO;
import com.petshop.entity.Employee;
import com.petshop.entity.Pet;
import com.petshop.entity.PetFood;
import com.petshop.entity.Supplier;
import com.petshop.repository.EmployeeRepository;
import com.petshop.repository.PetFoodRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class InventoryService {

    @Autowired private PetRepository petRepo;
    @Autowired private EmployeeRepository employeeRepo;
    @Autowired private PetFoodRepository foodRepo;
    @Autowired private SupplierRepository supplierRepo;

    // ===== FOOD =====

    public List<PetFood> getAllFood() {
        return foodRepo.findAll();
    }

    public PetFood getFoodById(int id) {
        return foodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found"));
    }

    public PetFood createFood(Map<String, Object> payload) {
        PetFood food = new PetFood();
        applyFood(food, payload);
        return foodRepo.save(food);
    }

    public PetFood updateFood(int id, Map<String, Object> payload) {
        PetFood food = getFoodById(id);
        applyFood(food, payload);
        return foodRepo.save(food);
    }

    // ===== SUPPLIER =====

    public List<Map<String, Object>> getAllSuppliers() {
        List<Object[]> rows = supplierRepo.findAllRaw();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("supplierId", row[0]);
            map.put("name", row[1]);
            map.put("contactPerson", row[2]);
            map.put("phoneNumber", row[3]);
            map.put("email", row[4]);
            result.add(map);
        }
        return result;
    }

    public Supplier getSupplierById(int id) {
        return supplierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public Supplier createSupplier(Map<String, Object> payload) {
        Supplier s = new Supplier();
        applySupplier(s, payload);
        return supplierRepo.save(s);
    }

    public Supplier updateSupplier(int id, Map<String, Object> payload) {
        Supplier s = getSupplierById(id);
        applySupplier(s, payload);
        return supplierRepo.save(s);
    }

    // ===== EMPLOYEE =====

    public List<Map<String, Object>> getAllEmployees() {
        return employeeRepo.findAll().stream().map(e -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("employeeId", e.getEmployeeId());
            map.put("firstName", e.getFirstName());
            map.put("lastName", e.getLastName());
            map.put("position", e.getPosition());
            map.put("phoneNumber", e.getPhoneNumber());
            return map;
        }).toList();
    }

    public Employee getEmployeeById(int id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee createEmployee(Map<String, Object> payload) {
        Employee e = new Employee();
        applyEmployee(e, payload);
        return employeeRepo.save(e);
    }

    public Employee updateEmployee(int id, Map<String, Object> payload) {
        Employee e = getEmployeeById(id);
        applyEmployee(e, payload);
        return employeeRepo.save(e);
    }

    // ===== ASSIGN =====

    public Map<String, Object> assignFood(int petId, int foodId) {
        Pet pet = petRepo.findById(petId).orElseThrow();
        PetFood food = foodRepo.findById(foodId).orElseThrow();

        pet.getFoods().add(food);
        petRepo.save(pet);

        return message("Food assigned");
    }

    public Map<String, Object> assignSupplier(int petId, int supplierId) {
        Pet pet = petRepo.findById(petId).orElseThrow();
        Supplier sup = supplierRepo.findById(supplierId).orElseThrow();

        pet.getSuppliers().add(sup);
        petRepo.save(pet);

        return message("Supplier assigned");
    }

    public Map<String, Object> assignEmployee(int petId, int employeeId) {
        Pet pet = petRepo.findById(petId).orElseThrow();
        Employee emp = employeeRepo.findById(employeeId).orElseThrow();

        pet.getEmployees().add(emp);
        petRepo.save(pet);

        return message("Employee assigned");
    }

    public List<PetFood> getPetFoods(int petId) {
        return petRepo.findById(petId).orElseThrow().getFoods();
    }

    public List<Supplier> getPetSuppliers(int petId) {
        return petRepo.findById(petId).orElseThrow().getSuppliers();
    }

    public List<Employee> getPetEmployees(int petId) {
        return petRepo.findById(petId).orElseThrow().getEmployees();
    }

    // ===== HELPERS =====

    private void applyFood(PetFood f, Map<String, Object> p) {
        f.setName((String) p.get("name"));
        f.setBrand((String) p.get("brand"));
        f.setType((String) p.get("type"));
        if (p.get("quantity") != null) f.setQuantity(Integer.parseInt(p.get("quantity").toString()));
        if (p.get("price") != null) f.setPrice(Double.parseDouble(p.get("price").toString()));
    }

    private void applySupplier(Supplier s, Map<String, Object> p) {
        s.setName((String) p.get("name"));
        s.setContactPerson((String) p.get("contactPerson"));
        s.setPhoneNumber((String) p.get("phoneNumber"));
        s.setEmail((String) p.get("email"));
    }

    private void applyEmployee(Employee e, Map<String, Object> p) {
        e.setFirstName((String) p.get("firstName"));
        e.setLastName((String) p.get("lastName"));
        e.setPosition((String) p.get("position"));
        e.setPhoneNumber((String) p.get("phoneNumber"));

        if (p.get("email") != null)
            e.setEmail((String) p.get("email"));

        if (p.get("hireDate") != null)
            e.setHireDate(java.time.LocalDate.parse(p.get("hireDate").toString()));
    }

    private Map<String, Object> message(String msg) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("message", msg);
        return m;
    }
}