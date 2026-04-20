package com.petshop.dto;

public class PetRequestDTO {

    private String name;
    private String breed;
    private int age;
    private double price;
    private String description;
    private String imageUrl;
    private int categoryId;

    public String getName() { return name; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getCategoryId() { return categoryId; }
}