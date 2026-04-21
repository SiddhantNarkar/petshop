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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository repo;
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public TransactionService(TransactionRepository repo,
                              CustomerRepository customerRepository,
                              PetRepository petRepository) {
        this.repo = repo;
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    public TransactionDTO createOrder(TransactionRequestDTO payload) {
        Customer customer = customerRepository.findById(payload.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + payload.getCustomerId()));

        Pet pet = petRepository.findById(payload.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pet not found with id: " + payload.getPetId()));

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setPet(pet);
        transaction.setAmount(payload.getAmount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setStatus(resolveStatus(payload.getStatus()));
        return toDTO(repo.save(transaction));
    }

    public TransactionDTO getTransactionById(int id) {
        return toDTO(findById(id));
    }

    public List<TransactionDTO> getOrdersByCustomer(int customerId) {
        return repo.findByCustomer_Id(customerId).stream()
                .map(this::toDTO)
                .toList();
    }

    public Map<String, Object> getCustomerSummary(int customerId) {
        List<TransactionDTO> list = getOrdersByCustomer(customerId);
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("customerId", customerId);
        summary.put("totalTransactions", list.size());
        summary.put("transactions", list);
        return summary;
    }

    public List<TransactionDTO> getAllTransactions() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public TransactionDTO updateStatus(int id, TransactionStatusUpdateDTO payload) {
        Transaction transaction = findById(id);
        transaction.setStatus(resolveStatus(payload.getStatus()));
        return toDTO(repo.save(transaction));
    }

    private Transaction findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    private TransactionDTO toDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getCustomer().getId(),
                transaction.getPet().getPetId(),
                transaction.getTransactionDate(),
                transaction.getAmount(),
                transaction.getStatus().name()
        );
    }

    private Transaction.Status resolveStatus(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Status must be either 'Success' or 'Failed'");
        }

        String value = raw.trim().toLowerCase();
        if ("success".equals(value)) {
            return Transaction.Status.Success;
        }
        if ("failed".equals(value) || "failure".equals(value)) {
            return Transaction.Status.Failed;
        }

        throw new IllegalArgumentException("Status must be either 'Success' or 'Failed'");
    }
}
