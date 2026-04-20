package com.petshop.dto;

public class SupplierDTO {

    private int supplierId;
    private String name;
    private String contactPerson;
    private String phoneNumber;
    private String email;

    public SupplierDTO(int supplierId, String name,
                       String contactPerson, String phoneNumber,
                       String email) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public String getContactPerson() { return contactPerson; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
}