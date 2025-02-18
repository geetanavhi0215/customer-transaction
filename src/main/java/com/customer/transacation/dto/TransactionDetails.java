package com.customer.transacation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionDetails {
    private Long transactionId;
    private LocalDate transactionDate;
    private BigDecimal amountSpent;
    private int rewardPoints;
}