package com.petshop.service;

import com.petshop.dto.TransactionDTO;
import com.petshop.entity.Transaction;
import com.petshop.exception.ResourceNotFoundException;
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
    @InjectMocks TransactionService transactionService;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        sampleTransaction = new Transaction();
        sampleTransaction.setId(1);
        sampleTransaction.setCustomerId(10);
        sampleTransaction.setPetId(5);
        sampleTransaction.setAmount(5000.0);
        sampleTransaction.setTransactionDate(LocalDate.now());
        sampleTransaction.setStatus(Transaction.Status.Success);
    }

    @Test
    @DisplayName("createOrder sets Success status for amount < 10000")
    void createOrder_lowAmount_setsSuccess() {
        Map<String, Object> payload = Map.of(
                "customerId", 10,
                "petId", 5,
                "amount", 5000.0
        );

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
    @DisplayName("createOrder sets Failed status for amount >= 10000")
    void createOrder_highAmount_setsFailed() {
        Map<String, Object> payload = Map.of(
                "customerId", 10,
                "petId", 5,
                "amount", 15000.0
        );

        when(repo.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(2);
            return t;
        });

        TransactionDTO result = transactionService.createOrder(payload);

        assertThat(result.getStatus()).isEqualTo("Failed");
    }

    @Test
    @DisplayName("createOrder sets Failed status for amount exactly 10000")
    void createOrder_exactBoundary_setsFailed() {
        Map<String, Object> payload = Map.of(
                "customerId", 1,
                "petId", 1,
                "amount", 10000.0
        );

        when(repo.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(3);
            return t;
        });

        TransactionDTO result = transactionService.createOrder(payload);

        assertThat(result.getStatus()).isEqualTo("Failed");
    }

    @Test
    @DisplayName("createOrder uses explicit status from payload when provided")
    void createOrder_withExplicitStatus_usesPayloadStatus() {
        Map<String, Object> payload = Map.of(
                "customerId", 1,
                "petId", 1,
                "amount", 500.0,
                "status", "failed"
        );

        when(repo.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(4);
            return t;
        });

        TransactionDTO result = transactionService.createOrder(payload);

        assertThat(result.getStatus()).isEqualTo("Failed");
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
        second.setId(2);
        second.setCustomerId(20);
        second.setPetId(8);
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
        when(repo.findByCustomerId(10)).thenReturn(List.of(sampleTransaction));

        List<TransactionDTO> result = transactionService.getOrdersByCustomer(10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerId()).isEqualTo(10);
    }

    @Test
    @DisplayName("getOrdersByCustomer returns empty list when no transactions found")
    void getOrdersByCustomer_noTransactions_returnsEmptyList() {
        when(repo.findByCustomerId(99)).thenReturn(List.of());

        List<TransactionDTO> result = transactionService.getOrdersByCustomer(99);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getCustomerSummary returns summary map with correct fields")
    void getCustomerSummary_returnsCorrectSummary() {
        when(repo.findByCustomerId(10)).thenReturn(List.of(sampleTransaction));

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
        when(repo.save(any(Transaction.class))).thenReturn(sampleTransaction);

        Map<String, Object> payload = Map.of("status", "failed");
        TransactionDTO result = transactionService.updateStatus(1, payload);

        assertThat(result.getStatus()).isEqualTo("Failed");
        verify(repo).save(sampleTransaction);
    }

    @Test
    @DisplayName("updateStatus throws exception when transaction not found")
    void updateStatus_notFound_throwsException() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.updateStatus(99, Map.of("status", "success")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction not found");
    }
}