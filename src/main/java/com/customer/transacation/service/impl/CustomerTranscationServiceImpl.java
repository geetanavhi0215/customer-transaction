package com.customer.transacation.service.impl;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.transacation.domain.CustomerTransaction;
import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.repository.CustomerTranscationRepository;
import com.customer.transacation.service.CustomerTranscationService;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class CustomerTranscationServiceImpl implements CustomerTranscationService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerTranscationServiceImpl.class);

	    @Autowired
	    private CustomerTranscationRepository customerTranscationRepository;

		public RewardResponseDTO calculateRewardPoints(Long customerId) {
			 List<CustomerTransaction> transactions = customerTranscationRepository.getTransactions().stream()
			            .filter(t -> t.getCustomerId().equals(customerId))
			            .collect(Collectors.toList());

			        Map<String, Integer> monthlyPoints = new HashMap<>();
			        int totalPoints = 0;

			        for (CustomerTransaction transaction : transactions) {
			            int points = calculatePoints(transaction.getAmountSpent());

			            
			            String month = Month.of(transaction.getTransactionDate().getMonthValue()).name();
			            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
			            totalPoints += points;
			        }


	        RewardResponseDTO rewardResponseDTO=new RewardResponseDTO();
	        rewardResponseDTO.setCustomerId(customerId);
	        rewardResponseDTO.setMonthlyPoints(monthlyPoints);
	        rewardResponseDTO.setTotalPoints(totalPoints);
	        return rewardResponseDTO;
	    }

	    private int calculatePoints(double amount) {
	        int points = 0;
	        if (amount > 100) {
	            points += 2 * (amount - 100);
	            amount = 100;
	        }
	        if (amount > 50) {
	            points += 1 * (amount - 50);
	        }
	        return points;
	    }
}
