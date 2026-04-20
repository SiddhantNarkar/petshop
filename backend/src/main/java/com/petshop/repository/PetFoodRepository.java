package com.petshop.repository;

import com.petshop.entity.PetFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetFoodRepository extends JpaRepository<PetFood, Integer> {}