package com.petshop.controller;

import com.petshop.dto.*;
import com.petshop.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers")
public class ApiV1CustomerAddressController {

    private final CustomerService service;

    public ApiV1CustomerAddressController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public CustomerDTO register(@RequestBody CustomerRequestDTO req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        return service.login(payload.get("email"), payload.get("phone"));
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable int id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable int id, @RequestBody CustomerRequestDTO req) {
        return service.update(id, req);
    }

    @GetMapping("/{id}/address")
    public AddressDTO getAddress(@PathVariable int id) {
        return service.getAddress(id);
    }

    @PostMapping("/{id}/address")
    public AddressDTO addAddress(@PathVariable int id, @RequestBody AddressRequestDTO req) {
        return service.addOrUpdateAddress(id, req);
    }

    @PutMapping("/addresses/{id}")
    public AddressDTO updateAddress(@PathVariable int id,
                                    @RequestBody AddressRequestDTO req) {
        return service.updateAddressById(id, req);
    }
}