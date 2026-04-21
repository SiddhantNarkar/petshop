package com.petshop.service;

import com.petshop.dto.TransactionDTO;
import com.petshop.dto.TransactionRequestDTO;
import com.petshop.dto.TransactionStatusUpdateDTO;
import com.petshop.entity.Customer;
import com.petshop.entity.Pet;
import com.petshop.entity.Transaction;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.CustomerRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock TransactionRepository repo;
    @Mock CustomerRepository customerRepository;
    @Mock PetRepository petRepository;
    @InjectMocks TransactionService transactionService;

    private Transaction sampleTransaction;
    private Customer sampleCustomer;
    private Pet samplePet;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setId(10);
        sampleCustomer.setFirstName("Bob");
        sampleCustomer.setLastName("Smith");

        samplePet = new Pet();
        samplePet.setPetId(5);
        samplePet.setName("Buddy");

        sampleTransaction = new Transaction();
        sampleTransaction.setId(1);
        sampleTransaction.setCustomer(sampleCustomer);
        sampleTransaction.setPet(samplePet);
        sampleTransaction.setAmount(5000.0);
        sampleTransaction.setTransactionDate(LocalDate.now());
        sampleTransaction.setStatus(Transaction.Status.Success);
    }

    @Test
    @DisplayName("createOrder sets Success status from request payload")
    void createOrder_successStatus_createsTransaction() {
        TransactionRequestDTO payload = request(10, 5, 5000.0, "Success");

        when(customerRepository.findById(10)).thenReturn(Optional.of(sampleCustomer));
        when(petRepository.findById(5)).thenReturn(Optional.of(samplePet));

        when(repo.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(1);
            return t;
        });

        TransactionDTO result = transactionService.createOrder(payload);

        assertThat(result.getStatus()).isEqualTo("Success");
        assertThat(result.getTransactionDate()).isEqualTo(LocalDate.now());
        assertThat(result.getCustomerId()).isEqualTo(10);
        assertThat(result.getPetId()).isEqualTo(5);
        verify(repo).save(any(Transaction.class));
    }

    @Test
    @DisplayName("createOrder sets Failed status from request payload")
    void createOrder_failedStatus_createsTransaction() {
        TransactionRequestDTO payload = request(10, 5, 15000.0, "Failed");

        when(customerRepository.findById(10)).thenReturn(Optional.of(sampleCustomer));
        when(petRepository.findById(5)).thenReturn(Optional.of(samplePet));

        when(repo.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(2);
            return t;
        });

        TransactionDTO result = transactionService.createOrder(payload);

        assertThat(result.getStatus()).isEqualTo("Failed");
    }

    @Test
    @DisplayName("createOrder throws exception when customer does not exist")
    void createOrder_missingCustomer_throwsException() {
        TransactionRequestDTO payload = request(99, 5, 10000.0, "Success");

        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createOrder(payload))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer not found with id: 99");
    }

    @Test
    @DisplayName("createOrder throws exception when pet does not exist")
    void createOrder_missingPet_throwsException() {
        TransactionRequestDTO payload = request(10, 99, 10000.0, "Success");

        when(customerRepository.findById(10)).thenReturn(Optional.of(sampleCustomer));
        when(petRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createOrder(payload))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pet not found with id: 99");
    }

    @Test
    @DisplayName("createOrder throws exception for invalid status")
    void createOrder_withInvalidStatus_throwsException() {
        TransactionRequestDTO payload = request(10, 5, 500.0, "Pending");

        when(customerRepository.findById(10)).thenReturn(Optional.of(sampleCustomer));
        when(petRepository.findById(5)).thenReturn(Optional.of(samplePet));

        assertThatThrownBy(() -> transactionService.createOrder(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Status must be either 'Success' or 'Failed'");
    }

    @Test
    @DisplayName("getTransactionById returns DTO when found")
    void getTransactionById_found_returnsDTO() {
        when(repo.findById(1)).thenReturn(Optional.of(sampleTransaction));

        TransactionDTO dto = transactionService.getTransactionById(1);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getCustomerId()).isEqualTo(10);
        assertThat(dto.getPetId()).isEqualTo(5);
        assertThat(dto.getAmount()).isEqualTo(5000.0);
        assertThat(dto.getStatus()).isEqualTo("Success");
    }

    @Test
    @DisplayName("getTransactionById throws exception when not found")
    void getTransactionById_notFound_throwsException() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction not found");
    }

    @Test
    @DisplayName("getAllTransactions returns list of DTOs")
    void getAllTransactions_returnsList() {
        Transaction second = new Transaction();
        Customer secondCustomer = new Customer();
        secondCustomer.setId(20);
        Pet secondPet = new Pet();
        secondPet.setPetId(8);
        second.setId(2);
        second.setCustomer(secondCustomer);
        second.setPet(secondPet);
        second.setAmount(8000.0);
        second.setTransactionDate(LocalDate.now());
        second.setStatus(Transaction.Status.Success);

        when(repo.findAll()).thenReturn(List.of(sampleTransaction, second));

        List<TransactionDTO> result = transactionService.getAllTransactions();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(0).getStatus()).isEqualTo("Success");
        assertThat(result.get(1).getStatus()).isEqualTo("Success");
    }

    @Test
    @DisplayName("getOrdersByCustomer returns list of DTOs for customer")
    void getOrdersByCustomer_returnsFilteredList() {
        when(repo.findByCustomer_Id(10)).thenReturn(List.of(sampleTransaction));

        List<TransactionDTO> result = transactionService.getOrdersByCustomer(10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerId()).isEqualTo(10);
    }

    @Test
    @DisplayName("getOrdersByCustomer returns empty list when no transactions found")
    void getOrdersByCustomer_noTransactions_returnsEmptyList() {
        when(repo.findByCustomer_Id(99)).thenReturn(List.of());

        List<TransactionDTO> result = transactionService.getOrdersByCustomer(99);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getCustomerSummary returns summary map with correct fields")
    void getCustomerSummary_returnsCorrectSummary() {
        when(repo.findByCustomer_Id(10)).thenReturn(List.of(sampleTransaction));

        Map<String, Object> summary = transactionService.getCustomerSummary(10);

        assertThat(summary).containsKey("customerId");
        assertThat(summary).containsKey("totalTransactions");
        assertThat(summary).containsKey("transactions");
        assertThat(summary.get("customerId")).isEqualTo(10);
        assertThat(summary.get("totalTransactions")).isEqualTo(1);
    }

    @Test
    @DisplayName("updateStatus changes status to Failed")
    void updateStatus_toFailed_updatesCorrectly() {
        when(repo.findById(1)).thenReturn(Optional.of(sampleTransaction));
        when(repo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        TransactionStatusUpdateDTO payload = new TransactionStatusUpdateDTO();
        payload.setStatus("failed");
        TransactionDTO result = transactionService.updateStatus(1, payload);

        assertThat(result.getStatus()).isEqualTo("Failed");
        verify(repo).save(sampleTransaction);
    }

    @Test
    @DisplayName("updateStatus throws exception when transaction not found")
    void updateStatus_notFound_throwsException() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        TransactionStatusUpdateDTO payload = new TransactionStatusUpdateDTO();
        payload.setStatus("success");

        assertThatThrownBy(() -> transactionService.updateStatus(99, payload))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction not found");
    }

    private TransactionRequestDTO request(int customerId, int petId, double amount, String status) {
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setCustomerId(customerId);
        request.setPetId(petId);
        request.setAmount(amount);
        request.setStatus(status);
        return request;
    }
}
