package com.petshop.service;

import com.petshop.dto.*;
import com.petshop.entity.*;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired private PetRepository petRepo;
    @Autowired private PetCategoryRepository categoryRepo;

    // CREATE
    public PetDTO createPet(PetRequestDTO req) {
        Pet pet = new Pet();

        pet.setName(req.getName());
        pet.setBreed(req.getBreed());
        pet.setAge(req.getAge());
        pet.setPrice(req.getPrice());
        pet.setDescription(req.getDescription());
        pet.setImageUrl(req.getImageUrl());

        PetCategory category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        pet.setCategory(category);

        return mapToDTO(petRepo.save(pet));
    }

    // GET ALL
    public List<PetDTO> getAllPets() {
        return petRepo.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    // GET BY ID
    public PetDTO getPetById(int id) {
        return mapToDTO(petRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found")));
    }

    // UPDATE
    public PetDTO updatePet(int id, PetRequestDTO req) {
        Pet pet = petRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        pet.setName(req.getName());
        pet.setBreed(req.getBreed());
        pet.setAge(req.getAge());
        pet.setPrice(req.getPrice());
        pet.setDescription(req.getDescription());
        pet.setImageUrl(req.getImageUrl());

        PetCategory category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        pet.setCategory(category);

        return mapToDTO(petRepo.save(pet));
    }

    // FILTER
    public List<PetDTO> getPetsByCategory(int categoryId) {
        return petRepo.findByCategory_CategoryId(categoryId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // MAPPER
    private PetDTO mapToDTO(Pet pet) {
        return new PetDTO(
                pet.getPetId(),
                pet.getName(),
                pet.getBreed(),
                pet.getAge(),
                pet.getPrice(),
                pet.getCategory() != null ? pet.getCategory().getName() : null
        );
    }

    public PetDetailsDTO getPetDetails(int id) {

        Pet pet = petRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        return new PetDetailsDTO(
                pet.getPetId(),
                pet.getName(),
                pet.getBreed(),
                pet.getAge(),
                pet.getPrice(),
                pet.getDescription(),
                pet.getImageUrl(),
                pet.getCategory() != null ? pet.getCategory().getName() : null,

                pet.getFoods() == null ? List.of() :
                        pet.getFoods().stream().map(f -> f.getName()).toList(),

                pet.getSuppliers() == null ? List.of() :
                        pet.getSuppliers().stream().map(s -> s.getName()).toList(),

                pet.getEmployees() == null ? List.of() :
                        pet.getEmployees().stream().map(e -> e.getFirstName()).toList(),

                pet.getGroomingServices() == null ? List.of() :
                        pet.getGroomingServices().stream().map(g -> g.getName()).toList(),

                pet.getVaccinations() == null ? List.of() :
                        pet.getVaccinations().stream().map(v -> v.getName()).toList()
        );
    }
}