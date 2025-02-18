package com.customer.transacation.service;

import java.util.Map;

import com.customer.transacation.dto.RewardResponseDTO;

public interface CustomerTransactionService {

	RewardResponseDTO calculateRewardPoints(Long customerId);

	Map<String, Integer> getMonthlyRewardPoints(Long customerId);

	Map<String, Object> getRewardPointsForMonth(Long customerId, String month);
	

}
