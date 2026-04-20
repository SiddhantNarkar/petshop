package com.petshop.dto;

import java.time.LocalDate;

public class TransactionDTO {

    private int id;
    private int customerId;
    private int petId;
    private LocalDate transactionDate;
    private double amount;
    private String status;

    public TransactionDTO(int id, int customerId, int petId,
                          LocalDate transactionDate, double amount, String status) {
        this.id = id;
        this.customerId = customerId;
        this.petId = petId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.status = status;
    }

    public int getId()                    { return id; }
    public int getCustomerId()            { return customerId; }
    public int getPetId()                 { return petId; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public double getAmount()             { return amount; }
    public String getStatus()             { return status; }
}