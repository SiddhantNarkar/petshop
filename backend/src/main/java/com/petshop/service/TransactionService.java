package com.petshop.service;

import com.petshop.dto.TransactionDTO;
import com.petshop.entity.Transaction;
import com.petshop.exception.ResourceNotFoundException;
import com.petshop.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    public TransactionDTO createOrder(Map<String, Object> payload) {
        Transaction t = new Transaction();
        t.setCustomerId(asInt(payload.get("customerId"), 0));
        t.setPetId(asInt(payload.get("petId"), 0));
        t.setAmount(asDouble(payload.get("amount"), 0.0));
        t.setTransactionDate(LocalDate.now());
        t.setStatus(resolveStatus(payload.get("status"), t.getAmount()));
        return toDTO(repo.save(t));
    }

    public TransactionDTO getTransactionById(int id) {
        return toDTO(findById(id));
    }

    public List<TransactionDTO> getOrdersByCustomer(int customerId) {
        return repo.findByCustomerId(customerId).stream().map(this::toDTO).toList();
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

    public TransactionDTO updateStatus(int id, Map<String, Object> payload) {
        Transaction t = findById(id);
        t.setStatus(resolveStatus(payload.get("status"), t.getAmount()));
        return toDTO(repo.save(t));
    }

    private Transaction findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    private TransactionDTO toDTO(Transaction t) {
        return new TransactionDTO(
                t.getId(), t.getCustomerId(), t.getPetId(),
                t.getTransactionDate(), t.getAmount(), t.getStatus().name());
    }

    private Transaction.Status resolveStatus(Object raw, double amount) {
        if (raw != null) {
            String v = String.valueOf(raw).trim().toLowerCase();
            if ("success".equals(v)) return Transaction.Status.Success;
            if ("failed".equals(v) || "failure".equals(v)) return Transaction.Status.Failed;
        }
        return amount < 10000 ? Transaction.Status.Success : Transaction.Status.Failed;
    }

    private int asInt(Object value, int def) {
        if (value == null) return def;
        try { return Integer.parseInt(String.valueOf(value).trim()); }
        catch (NumberFormatException e) { return def; }
    }

    private double asDouble(Object value, double def) {
        if (value == null) return def;
        try { return Double.parseDouble(String.valueOf(value).trim()); }
        catch (NumberFormatException e) { return def; }
    }
}