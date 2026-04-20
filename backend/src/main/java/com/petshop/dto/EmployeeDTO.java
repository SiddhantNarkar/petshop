package com.petshop.dto;

import java.time.LocalDate;

public class EmployeeDTO {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String phoneNumber;
    private String email;
    private LocalDate hireDate;

    public EmployeeDTO(int employeeId, String firstName, String lastName,
                       String position, String phoneNumber,
                       String email, LocalDate hireDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hireDate = hireDate;
    }

    public int getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPosition() { return position; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public LocalDate getHireDate() { return hireDate; }
}