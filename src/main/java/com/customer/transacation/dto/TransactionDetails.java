package com.customer.transacation.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionDetails {
    private Long transactionId;
    private LocalDate transactionDate;
    private double amountSpent;
    private int rewardPoints;
}