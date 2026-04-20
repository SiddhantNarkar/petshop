package com.petshop.controller;

import com.petshop.dto.TransactionDTO;
import com.petshop.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class ApiV1TransactionsController {

    private final TransactionService transactionService;

    public ApiV1TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/orders")
    public TransactionDTO createOrder(@RequestBody Map<String, Object> payload) {
        return transactionService.createOrder(payload);
    }

    @GetMapping("/orders/{id}")
    public TransactionDTO getOrderById(@PathVariable int id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/customers/{id}/orders")
    public List<TransactionDTO> getOrdersByCustomer(@PathVariable int id) {
        return transactionService.getOrdersByCustomer(id);
    }

    @GetMapping
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable int id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/customers/{id}")
    public Map<String, Object> getCustomerSummary(@PathVariable int id) {
        return transactionService.getCustomerSummary(id);
    }

    @PutMapping("/{id}/status")
    public TransactionDTO updateStatus(@PathVariable int id,
                                       @RequestBody Map<String, Object> payload) {
        return transactionService.updateStatus(id, payload);
    }
}