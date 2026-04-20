package com.petshop.dto;

public class AddressDTO {

    private int id;
    private String street;
    private String city;
    private String state;
    private String pincode;

    public AddressDTO(int id, String street, String city, String state, String pincode) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public int getId() { return id; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPincode() { return pincode; }
}