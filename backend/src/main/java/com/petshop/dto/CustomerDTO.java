package com.petshop.dto;

import com.petshop.dto.AddressDTO;

public class CustomerDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDTO address;

    public CustomerDTO(int id, String firstName, String lastName,
                       String email, String phone, AddressDTO address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public AddressDTO getAddress() { return address; }
}