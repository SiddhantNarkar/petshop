package com.petshop.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int id;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "pet_id")
    private int petId;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "amount")
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private Status status;

    public enum Status {
        Success,
        Failed
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getCustomerId()                  { return customerId; }
    public void setCustomerId(int customerId)   { this.customerId = customerId; }
    public int getPetId()                       { return petId; }
    public void setPetId(int petId)             { this.petId = petId; }
    public LocalDate getTransactionDate()       { return transactionDate; }
    public void setTransactionDate(LocalDate d) { this.transactionDate = d; }
    public double getAmount()                   { return amount; }
    public void setAmount(double amount)        { this.amount = amount; }
    public Status getStatus()                   { return status; }
    public void setStatus(Status status)        { this.status = status; }
}