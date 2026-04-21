package com.petshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransactionRequestDTO {

    @NotNull(message = "customerId is required")
    @Positive(message = "customerId must be a positive number")
    private Integer customerId;

    @NotNull(message = "petId is required")
    @Positive(message = "petId must be a positive number")
    private Integer petId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "status is required")
    private String status;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
