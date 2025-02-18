package com.customer.transacation.service.impl;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.transacation.dto.MonthlyRewardDetails;
import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.dto.TransactionDetails;
import com.customer.transacation.entity.CustomerTransaction;
import com.customer.transacation.repository.CustomerTransactionRepository;
import com.customer.transacation.service.CustomerTransactionService;
import com.customer.transaction.errors.CustomerTransactionException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerTransactionServiceImpl implements CustomerTransactionService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerTransactionServiceImpl.class);

	@Autowired
	private CustomerTransactionRepository customerTranscationRepository;

	/**
	 * Calculates the total reward points for a specific customer based on their
	 * transaction history. Fetches transactions, calculates points for each, and
	 * summarizes monthly and total points.
	 * 
	 * @param customerId the ID of the customer whose reward points are to be
	 *                   calculated
	 * @return RewardResponseDTO containing total points, monthly points, and
	 *         transaction details
	 */
	public RewardResponseDTO calculateRewardPoints(Long customerId) {
		validateCustomerExists(customerId);	

		Map<String, List<CustomerTransaction>> transactionsByMonth = customerTranscationRepository.getTransactions()
				.stream().filter(t -> t.getCustomerId().equals(customerId))
				.collect(Collectors.groupingBy(t -> Month.of(t.getTransactionDate().getMonthValue()).name()));

		List<MonthlyRewardDetails> monthlyRewards = new ArrayList<>();
		int totalPoints = 0;

		for (Map.Entry<String, List<CustomerTransaction>> entry : transactionsByMonth.entrySet()) {
			String month = entry.getKey();
			List<CustomerTransaction> transactions = entry.getValue();

			int monthlyPoints = 0;
			List<TransactionDetails> transactionDetailsList = new ArrayList<>();

			for (CustomerTransaction transaction : transactions) {
				int points = calculatePoints(transaction.getAmountSpent());
				monthlyPoints += points;

				TransactionDetails details = new TransactionDetails();
				details.setTransactionId(transaction.getId());
				details.setTransactionDate(transaction.getTransactionDate());
				details.setAmountSpent(BigDecimal.valueOf(transaction.getAmountSpent()));
				details.setRewardPoints(points);

				transactionDetailsList.add(details);
			}

			MonthlyRewardDetails monthlyRewardDetails = new MonthlyRewardDetails();
			monthlyRewardDetails.setMonth(month);
			monthlyRewardDetails.setRewardPoints(monthlyPoints);
			monthlyRewardDetails.setTransactions(transactionDetailsList);

			monthlyRewards.add(monthlyRewardDetails);
			totalPoints += monthlyPoints;
		}

		RewardResponseDTO rewardResponseDTO = new RewardResponseDTO();
		rewardResponseDTO.setCustomerId(customerId);
		rewardResponseDTO.setTotalPoints(totalPoints);
		rewardResponseDTO.setMonthlyRewardDetails(monthlyRewards);

		LOG.info("Successfully calculated reward points for customerId: {}", customerId);
		return rewardResponseDTO;
	}

	/**
	 * Retrieves the monthly reward points for a specific customer.
	 * 
	 * @param customerId the ID of the customer whose monthly reward points are to
	 *                   be retrieved
	 * @return a Map where the keys are month names, and the values are reward
	 *         points
	 */
	@Override
	public Map<String, Integer> getMonthlyRewardPoints(Long customerId) {
		validateCustomerExists(customerId);	

		RewardResponseDTO response = calculateRewardPoints(customerId);
		LOG.info("Successfully calculated monthly reward points for customerId: {}", customerId);

		return response.getMonthlyRewardDetails().stream()
				.collect(Collectors.toMap(MonthlyRewardDetails::getMonth, MonthlyRewardDetails::getRewardPoints));
	}

	/**
	 * Calculates reward points for a single transaction based on the amount spent.
	 * Points are calculated as: - 2 points for every dollar spent over $100 - 1
	 * point for every dollar spent over $50
	 * 
	 * @param amount the transaction amount
	 * @return the calculated reward points
	 */
	public int calculatePoints(double amount) {
		int points = 0;
		if (amount < 0) {
            LOG.error("Invalid transaction amount: {}", amount);
            throw new CustomerTransactionException("Invalid transaction amount");
        }
		if (amount > 100) {
			points += 2 * (amount - 100);
			amount = 100;
		}
		if (amount > 50) {
			points += (amount - 50);
		}
		return points;
	}

	/**
	 * Retrieves the reward points for a specific customer for a given month.
	 * 
	 * @param customerId the ID of the customer
	 * @param month      the month for which the reward points are to be fetched
	 * @return a Map containing the month and the reward points for that month,
	 *         along with transaction details
	 */
	public Map<String, Object> getRewardPointsForMonth(Long customerId, String month) {
		validateCustomerExists(customerId);	
		
		List<CustomerTransaction> transactions = customerTranscationRepository.getTransactions().stream()
				.filter(t -> t.getCustomerId().equals(customerId)
						&& Month.of(t.getTransactionDate().getMonthValue()).name().equalsIgnoreCase(month))
				.collect(Collectors.toList());
		if (transactions.isEmpty()) {
            LOG.warn("No transactions found for customerId: {} in month: {}", customerId, month);
            throw new CustomerTransactionException("No transactions found for the specified month.");
        }
		int totalPoints = 0;
		List<TransactionDetails> transactionDetailsList = new ArrayList<>();
		for (CustomerTransaction transaction : transactions) {
			int points = calculatePoints(transaction.getAmountSpent());
			totalPoints += points;

			TransactionDetails details = new TransactionDetails();
			details.setTransactionId(transaction.getId());
			details.setTransactionDate(transaction.getTransactionDate());
			details.setAmountSpent(BigDecimal.valueOf(transaction.getAmountSpent()));
			details.setRewardPoints(points);

			transactionDetailsList.add(details);
		}
		Map<String, Object> monthlyRewardData = new HashMap<>();
		monthlyRewardData.put("month", month);
		monthlyRewardData.put("totalPoints", totalPoints);
		monthlyRewardData.put("transactions", transactionDetailsList);

		return monthlyRewardData;
	}
	/**
    * Validates if a customer exists by their ID.
    * 
    * @param customerId the ID of the customer to validate
    * @throws CustomerTransactionException if the customer does not exist
    */
   public void validateCustomerExists(Long customerId) {
       boolean customerExists = customerTranscationRepository.existsById(customerId);
       if (!customerExists) {
           throw new CustomerTransactionException("CustomerId not found: " + customerId);
       }
   }
}
