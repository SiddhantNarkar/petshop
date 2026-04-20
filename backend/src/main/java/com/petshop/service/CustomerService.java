package com.petshop.service;

import com.petshop.dto.*;
import com.petshop.entity.*;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepo;
    private final AddressRepository addressRepo;

    public CustomerService(CustomerRepository customerRepo, AddressRepository addressRepo) {
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
    }

    // REGISTER
    public CustomerDTO register(CustomerRequestDTO req) {
        Customer c = new Customer();
        c.setFirstName(req.getFirstName());
        c.setLastName(req.getLastName());
        c.setEmail(req.getEmail());
        c.setPhone(req.getPhone());

        return mapToDTO(customerRepo.save(c));
    }

    // LOGIN
    public Map<String, Object> login(String email, String phone) {
        Customer c = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (phone != null && !phone.equals(c.getPhone())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("message", "Login successful");
        res.put("customerId", c.getId());
        res.put("customer", mapToDTO(c));
        return res;
    }

    // GET CUSTOMER
    public CustomerDTO getById(int id) {
        Customer c = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return mapToDTO(c);
    }

    // UPDATE CUSTOMER
    public CustomerDTO update(int id, CustomerRequestDTO req) {
        Customer c = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (req.getFirstName() != null) c.setFirstName(req.getFirstName());
        if (req.getLastName() != null) c.setLastName(req.getLastName());
        if (req.getEmail() != null) c.setEmail(req.getEmail());
        if (req.getPhone() != null) c.setPhone(req.getPhone());

        return mapToDTO(customerRepo.save(c));
    }

    // GET ADDRESS
    public AddressDTO getAddress(int id) {
        Customer c = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (c.getAddress() == null) {
            throw new ResourceNotFoundException("Address not found");
        }

        Address a = c.getAddress();
        return new AddressDTO(
                a.getId(),
                a.getStreet(),
                a.getCity(),
                a.getState(),
                a.getPincode()
        );
    }

    // ADD/UPDATE ADDRESS
    public AddressDTO addOrUpdateAddress(int id, AddressRequestDTO req) {
        Customer c = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Address a = c.getAddress() != null ? c.getAddress() : new Address();

        a.setStreet(req.getStreet());
        a.setCity(req.getCity());
        a.setState(req.getState());
        a.setPincode(req.getPincode());

        Address saved = addressRepo.save(a);
        c.setAddress(saved);
        customerRepo.save(c);

        return new AddressDTO(
                saved.getId(),
                saved.getStreet(),
                saved.getCity(),
                saved.getState(),
                saved.getPincode()
        );
    }

    // MAPPER
    private CustomerDTO mapToDTO(Customer c) {

        AddressDTO addressDTO = null;

        if (c.getAddress() != null) {
            Address a = c.getAddress();
            addressDTO = new AddressDTO(
                    a.getId(),
                    a.getStreet(),
                    a.getCity(),
                    a.getState(),
                    a.getPincode()
            );
        }

        return new CustomerDTO(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhone(),
                addressDTO
        );
    }

    public AddressDTO updateAddressById(int addressId, AddressRequestDTO req) {

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (req.getStreet() != null) address.setStreet(req.getStreet());
        if (req.getCity() != null) address.setCity(req.getCity());
        if (req.getState() != null) address.setState(req.getState());
        if (req.getPincode() != null) address.setPincode(req.getPincode());

        Address saved = addressRepo.save(address);

        return new AddressDTO(
                saved.getId(),
                saved.getStreet(),
                saved.getCity(),
                saved.getState(),
                saved.getPincode()
        );
    }
}