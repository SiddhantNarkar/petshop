package com.petshop.repository;

import com.petshop.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    // Filter by category
    List<Pet> findByCategory_CategoryId(int categoryId);
}