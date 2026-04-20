package com.petshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "grooming_services")
public class GroomingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private int id;

    private String name;
    private String description;
    private double price;
    private boolean available = true;

    @JsonIgnore
    @ManyToMany(mappedBy = "groomingServices")
    private List<Pet> pets;

    public int getId()                      { return id; }
    public void setId(int id)              { this.id = id; }
    public String getName()                { return name; }
    public void setName(String name)       { this.name = name; }
    public String getDescription()         { return description; }
    public void setDescription(String d)   { this.description = d; }
    public double getPrice()               { return price; }
    public void setPrice(double price)     { this.price = price; }
    public boolean isAvailable()           { return available; }
    public void setAvailable(boolean a)    { this.available = a; }
    public List<Pet> getPets()             { return pets; }
    public void setPets(List<Pet> pets)    { this.pets = pets; }
}