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
import com.customer.transacation.dto.TransactionDetails;
import com.customer.transacation.repository.CustomerTranscationRepository;
import com.customer.transacation.service.CustomerTransactionService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerTranscationServiceImpl implements CustomerTransactionService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerTranscationServiceImpl.class);

	@Autowired
	private CustomerTranscationRepository customerTranscationRepository;

	@Autowired
	private ValidationServiceImpl validationServiceImpl;

	/**
	 * Calculates the total reward points for a specific customer based on their transaction history.
	 * Fetches transactions, calculates points for each, and summarizes monthly and total points.
	 * 
	 * @param customerId the ID of the customer whose reward points are to be calculated
	 * @return RewardResponseDTO containing total points, monthly points, and transaction details
	 */
	public RewardResponseDTO calculateRewardPoints(Long customerId) {
		validationServiceImpl.validateCustomerIdIsPresent(customerId);

		List<CustomerTransaction> transactions = customerTranscationRepository.getTransactions().stream()
				.filter(t -> t.getCustomerId().equals(customerId)).collect(Collectors.toList());
		Map<String, Integer> monthlyPoints = new HashMap<>();
		List<TransactionDetails> transactionDetailsList = transactions.stream().map(transaction -> {
			int points = calculatePoints(transaction.getAmountSpent());
			String month = Month.of(transaction.getTransactionDate().getMonthValue()).name();
			monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);

			TransactionDetails details = new TransactionDetails();
			details.setTransactionId(transaction.getId());
			details.setTransactionDate(transaction.getTransactionDate());
			details.setAmountSpent(transaction.getAmountSpent().doubleValue());
			details.setRewardPoints(points);

			return details;
		}).collect(Collectors.toList());
		int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();

		RewardResponseDTO rewardResponseDTO = new RewardResponseDTO();
		rewardResponseDTO.setCustomerId(customerId);
		rewardResponseDTO.setMonthlyPoints(monthlyPoints);
		rewardResponseDTO.setTotalPoints(totalPoints);
		rewardResponseDTO.setTransactions(transactionDetailsList);
		LOG.info("Successfully calculated reward points for customerId: {}", customerId);
		return rewardResponseDTO;
	}
	
	
	/**
	 * Retrieves the monthly reward points for a specific customer.
	 * 
	 * @param customerId the ID of the customer whose monthly reward points are to be retrieved
	 * @return a Map where the keys are month names, and the values are reward points
	 */
	@Override
	public Map<String, Integer> getMonthlyRewardPoints(Long customerId) {
		validationServiceImpl.validateCustomerIdIsPresent(customerId);

		RewardResponseDTO response = calculateRewardPoints(customerId);
		LOG.info("Successfully calculated monthly reward points for customerId: {}", customerId);
		return response.getMonthlyPoints();
	}

	
	/**
	 * Calculates reward points for a single transaction based on the amount spent.
	 * Points are calculated as:
	 * - 2 points for every dollar spent over $100
	 * - 1 point for every dollar spent over $50
	 * 
	 * @param amount the transaction amount
	 * @return the calculated reward points
	 */
	private int calculatePoints(double amount) {
		int points = 0;
		if (amount > 100) {
			points += 2 * (amount - 100);
			amount = 100;
		}
		if (amount > 50) {
			points += (amount - 50);
		}
		return points;
	}
}
