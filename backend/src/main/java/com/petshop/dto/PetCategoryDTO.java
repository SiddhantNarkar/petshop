package com.petshop.dto;

public class PetCategoryDTO {

    private int categoryId;
    private String name;

    public PetCategoryDTO(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
}