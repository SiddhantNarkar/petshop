package com.petshop.service;

import com.petshop.dto.*;
import com.petshop.entity.Customer;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock CustomerRepository customerRepo;
    @InjectMocks CustomerService customerService;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setId(1);
        sampleCustomer.setFirstName("John");
        sampleCustomer.setLastName("Doe");
        sampleCustomer.setEmail("john@example.com");
        sampleCustomer.setPhone("9876543210");
    }


    @Test
    @DisplayName("register saves and returns DTO")
    void register_savesAndReturnsDTO() {

        CustomerRequestDTO req = new CustomerRequestDTO();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setEmail("john@example.com");
        req.setPhone("9876543210");

        when(customerRepo.save(any(Customer.class))).thenReturn(sampleCustomer);

        CustomerDTO result = customerService.register(req);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(customerRepo).save(any(Customer.class));
    }


    @Test
    @DisplayName("getById returns DTO when found")
    void getById_found_returnsDTO() {

        when(customerRepo.findById(1)).thenReturn(Optional.of(sampleCustomer));

        CustomerDTO dto = customerService.getById(1);

        assertThat(dto).isNotNull();
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
    }


    @Test
    @DisplayName("getById throws exception when not found")
    void getById_notFound_throwsException() {

        when(customerRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer not found");
    }


    @Test
    @DisplayName("update updates fields and returns DTO")
    void update_updatesAndReturnsDTO() {

        CustomerRequestDTO req = new CustomerRequestDTO();
        req.setFirstName("Jane");
        req.setLastName("Smith");
        req.setEmail("jane@example.com");
        req.setPhone("1234567890");

        when(customerRepo.findById(1)).thenReturn(Optional.of(sampleCustomer));
        when(customerRepo.save(any(Customer.class))).thenReturn(sampleCustomer);

        CustomerDTO result = customerService.update(1, req);

        assertThat(result).isNotNull();
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    @DisplayName("update throws exception when customer not found")
    void update_notFound_throwsException() {

        CustomerRequestDTO req = new CustomerRequestDTO();

        when(customerRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.update(99, req))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    @DisplayName("login returns success response")
    void login_success() {

        when(customerRepo.findByEmail("john@example.com"))
                .thenReturn(Optional.of(sampleCustomer));

        var result = customerService.login("john@example.com", "9876543210");

        assertThat(result).isNotNull();
        assertThat(result.get("message")).isEqualTo("Login successful");
        assertThat(result.get("customerId")).isEqualTo(1);
    }


    @Test
    @DisplayName("login throws exception for invalid credentials")
    void login_invalidCredentials() {

        when(customerRepo.findByEmail("wrong@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                customerService.login("wrong@example.com", "123"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}