package com.customer.transacation.service;

import com.customer.transacation.dto.RewardResponseDTO;

public interface CustomerTranscationService {

	RewardResponseDTO calculateRewardPoints(Long customerId);
	

}
