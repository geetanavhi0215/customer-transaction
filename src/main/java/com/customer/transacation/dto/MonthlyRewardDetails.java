package com.customer.transacation.dto;

import java.util.List;

import lombok.Data;

@Data
public class MonthlyRewardDetails {
	private String month; 
    private int rewardPoints; 
    private List<TransactionDetails> transactions; 
}