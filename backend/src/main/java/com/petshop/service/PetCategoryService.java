package com.petshop.service;

import com.petshop.dto.*;
import com.petshop.entity.PetCategory;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.PetCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetCategoryService {

    @Autowired
    private PetCategoryRepository categoryRepo;

    public PetCategoryDTO createCategory(PetCategoryRequestDTO req) {
        PetCategory category = new PetCategory();
        category.setName(req.getName());
        return mapToDTO(categoryRepo.save(category));
    }

    public List<PetCategoryDTO> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public PetCategoryDTO getCategoryById(int id) {
        return mapToDTO(categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }

    private PetCategoryDTO mapToDTO(PetCategory c) {
        return new PetCategoryDTO(c.getCategoryId(), c.getName());
    }

    public PetCategoryDTO updateCategory(int id, PetCategoryRequestDTO req) {
        PetCategory category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(req.getName());

        return mapToDTO(categoryRepo.save(category));
    }
}