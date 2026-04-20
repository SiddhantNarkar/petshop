package com.petshop.controller;

import com.petshop.dto.ServiceDTO;
import com.petshop.service.ServiceManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/services")
public class ApiV1ServicesController {

    private final ServiceManager serviceManager;

    public ApiV1ServicesController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @GetMapping("/grooming")
    public List<ServiceDTO> getAllGroomingServices() {
        return serviceManager.getAllGrooming();
    }

    @PostMapping("/grooming")
    public ServiceDTO createGroomingService(@RequestBody Map<String, Object> payload) {
        return serviceManager.createGrooming(payload);
    }

    @PostMapping("/pets/{petId}/grooming/{serviceId}")
    public Map<String, Object> assignGroomingService(@PathVariable int petId, @PathVariable int serviceId) {
        return serviceManager.assignGrooming(petId, serviceId);
    }

    @GetMapping("/pets/{petId}/grooming")
    public List<ServiceDTO> getPetGroomingServices(@PathVariable int petId) {
        return serviceManager.getPetGroomingServices(petId);
    }

    @GetMapping("/vaccinations")
    public List<ServiceDTO> getAllVaccinations() {
        return serviceManager.getAllVaccinations();
    }

    @PostMapping("/vaccinations")
    public ServiceDTO createVaccination(@RequestBody Map<String, Object> payload) {
        return serviceManager.createVaccination(payload);
    }

    @PostMapping("/pets/{petId}/vaccinations/{vaccinationId}")
    public Map<String, Object> assignVaccination(@PathVariable int petId, @PathVariable int vaccinationId) {
        return serviceManager.assignVaccination(petId, vaccinationId);
    }

    @GetMapping("/pets/{petId}/vaccinations")
    public List<ServiceDTO> getPetVaccinations(@PathVariable int petId) {
        return serviceManager.getPetVaccinations(petId);
    }
}