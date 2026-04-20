package com.petshop.dto;

public class PetFoodDTO {

    private int foodId;
    private String name;
    private String brand;
    private String type;
    private int quantity;
    private double price;

    public PetFoodDTO(int foodId, String name, String brand,
                      String type, int quantity, double price) {
        this.foodId = foodId;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public int getFoodId() { return foodId; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}