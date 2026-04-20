package com.petshop.dto;

import java.util.List;

public class PetDetailsDTO {

    private int petId;
    private String name;
    private String breed;
    private int age;
    private double price;
    private String description;
    private String imageUrl;
    private String category;

    private List<String> foods;
    private List<String> suppliers;
    private List<String> employees;
    private List<String> groomingServices;
    private List<String> vaccinations;

    public PetDetailsDTO(int petId, String name, String breed, int age, double price,
                         String description, String imageUrl, String category,
                         List<String> foods, List<String> suppliers,
                         List<String> employees, List<String> groomingServices,
                         List<String> vaccinations) {
        this.petId = petId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.foods = foods;
        this.suppliers = suppliers;
        this.employees = employees;
        this.groomingServices = groomingServices;
        this.vaccinations = vaccinations;
    }

    public int getPetId() { return petId; }
    public String getName() { return name; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public List<String> getFoods() { return foods; }
    public List<String> getSuppliers() { return suppliers; }
    public List<String> getEmployees() { return employees; }
    public List<String> getGroomingServices() { return groomingServices; }
    public List<String> getVaccinations() { return vaccinations; }
}