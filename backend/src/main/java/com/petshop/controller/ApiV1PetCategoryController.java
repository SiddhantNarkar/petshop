package com.petshop.controller;

import com.petshop.dto.*;
import com.petshop.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApiV1PetCategoryController {

    private final PetService petService;
    private final PetCategoryService categoryService;

    public ApiV1PetCategoryController(PetService petService,
                                      PetCategoryService categoryService) {
        this.petService = petService;
        this.categoryService = categoryService;
    }

    // ===== CATEGORY =====

    @GetMapping("/categories")
    public List<PetCategoryDTO> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/categories")
    public PetCategoryDTO createCategory(@RequestBody PetCategoryRequestDTO req) {
        return categoryService.createCategory(req);
    }

    @GetMapping("/categories/{id}")
    public PetCategoryDTO getCategory(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/categories/{id}")
    public PetCategoryDTO updateCategory(@PathVariable int id,
                                         @RequestBody PetCategoryRequestDTO req) {
        return categoryService.updateCategory(id, req);
    }

    // ===== PET =====

    @GetMapping("/pets")
    public List<PetDTO> getPets() {
        return petService.getAllPets();
    }

    @GetMapping("/pets/{id}")
    public PetDTO getPet(@PathVariable int id) {
        return petService.getPetById(id);
    }

    @PostMapping("/pets")
    public PetDTO createPet(@RequestBody PetRequestDTO req) {
        return petService.createPet(req);
    }

    @PutMapping("/pets/{id}")
    public PetDTO updatePet(@PathVariable int id, @RequestBody PetRequestDTO req) {
        return petService.updatePet(id, req);
    }

    @GetMapping("/pets/categories/{categoryId}")
    public List<PetDTO> getPetsByCategory(@PathVariable int categoryId) {
        return petService.getPetsByCategory(categoryId);
    }

    @GetMapping("/pets/{id}/details")
    public PetDetailsDTO getPetDetails(@PathVariable int id) {
        return petService.getPetDetails(id);
    }
}