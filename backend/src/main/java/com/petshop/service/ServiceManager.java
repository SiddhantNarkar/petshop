package com.petshop.service;

import com.petshop.dto.ServiceDTO;
import com.petshop.entity.GroomingService;
import com.petshop.entity.Pet;
import com.petshop.entity.Vaccination;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.GroomingServiceRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.VaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceManager {

    @Autowired
    private GroomingServiceRepository groomingRepo;

    @Autowired
    private VaccinationRepository vaccinationRepo;

    @Autowired
    private PetRepository petRepo;

    public ServiceDTO createGrooming(Map<String, Object> payload) {
        GroomingService g = new GroomingService();
        applyGroomingPayload(g, payload);
        return toGroomingDTO(groomingRepo.save(g));
    }

    public ServiceDTO createVaccination(Map<String, Object> payload) {
        Vaccination v = new Vaccination();
        applyVaccinationPayload(v, payload);
        return toVaccinationDTO(vaccinationRepo.save(v));
    }

    public Map<String, Object> assignGrooming(int petId, int serviceId) {
        Pet pet = petRepo.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        GroomingService g = groomingRepo.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Grooming service not found"));
        if (pet.getGroomingServices() == null) pet.setGroomingServices(new ArrayList<>());
        pet.getGroomingServices().add(g);
        petRepo.save(pet);
        return message("Grooming service assigned");
    }

    public Map<String, Object> assignVaccination(int petId, int vaccinationId) {
        Pet pet = petRepo.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        Vaccination v = vaccinationRepo.findById(vaccinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccination not found"));
        if (pet.getVaccinations() == null) pet.setVaccinations(new ArrayList<>());
        pet.getVaccinations().add(v);
        petRepo.save(pet);
        return message("Vaccination assigned");
    }

    public List<ServiceDTO> getAllGrooming() {
        return groomingRepo.findAll().stream().map(this::toGroomingDTO).toList();
    }

    public List<ServiceDTO> getAllVaccinations() {
        return vaccinationRepo.findAll().stream().map(this::toVaccinationDTO).toList();
    }

    public List<ServiceDTO> getPetGroomingServices(int petId) {
        Pet pet = petRepo.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        if (pet.getGroomingServices() == null) return List.of();
        return pet.getGroomingServices().stream().map(this::toGroomingDTO).toList();
    }

    public List<ServiceDTO> getPetVaccinations(int petId) {
        Pet pet = petRepo.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        if (pet.getVaccinations() == null) return List.of();
        return pet.getVaccinations().stream().map(this::toVaccinationDTO).toList();
    }

    private ServiceDTO toGroomingDTO(GroomingService g) {
        return new ServiceDTO(g.getId(), g.getName(), g.getDescription(), g.getPrice(), g.isAvailable());
    }

    private ServiceDTO toVaccinationDTO(Vaccination v) {
        return new ServiceDTO(v.getId(), v.getName(), v.getDescription(), v.getPrice(), v.isAvailable());
    }

    private void applyGroomingPayload(GroomingService g, Map<String, Object> p) {
        if (p.containsKey("name"))        g.setName(asString(p.get("name")));
        if (p.containsKey("description")) g.setDescription(asString(p.get("description")));
        if (p.containsKey("price"))       g.setPrice(asDouble(p.get("price"), g.getPrice()));
        if (p.containsKey("available"))   g.setAvailable(asBoolean(p.get("available"), g.isAvailable()));
    }

    private void applyVaccinationPayload(Vaccination v, Map<String, Object> p) {
        if (p.containsKey("name"))        v.setName(asString(p.get("name")));
        if (p.containsKey("description")) v.setDescription(asString(p.get("description")));
        if (p.containsKey("price"))       v.setPrice(asDouble(p.get("price"), v.getPrice()));
        if (p.containsKey("available"))   v.setAvailable(asBoolean(p.get("available"), v.isAvailable()));
    }

    private Map<String, Object> message(String text) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("message", text);
        return m;
    }

    private String asString(Object v)                 { return v == null ? "" : String.valueOf(v).trim(); }
    private double asDouble(Object v, double def)     { try { return Double.parseDouble(String.valueOf(v).trim()); } catch (Exception e) { return def; } }
    private boolean asBoolean(Object v, boolean def)  { return v == null ? def : Boolean.parseBoolean(String.valueOf(v).trim()); }
}