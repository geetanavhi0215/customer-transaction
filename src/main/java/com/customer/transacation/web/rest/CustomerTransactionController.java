package com.customer.transacation.web.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.service.CustomerTransactionService;

@RestController
@RequestMapping("/api/customer-transactions")
public class CustomerTransactionController {


	private static final Logger log = LoggerFactory.getLogger(CustomerTransactionController.class);

	private static final String ENTITY_NAME = "CustomerTranscation";

	@Autowired
	private CustomerTransactionService customerTranscationService;


	public CustomerTransactionController(CustomerTransactionService customerTranscationService) {
		this.customerTranscationService = customerTranscationService;
	}

	 /**
	 * Endpoint to fetch the total reward points for a specific customer. Logs the request and delegates the reward calculation to the service layer.
	 * @param customerId the ID of the customer whose reward points are to be fetched
	 * @return a ResponseEntity containing the RewardResponseDTO with reward details
	 */ 
	@GetMapping("/{customerId}")
	public ResponseEntity<RewardResponseDTO>  getRewardPoints(@PathVariable Long customerId) {
		log.info("Received request to fetch reward points for customerId: {}", customerId);
		RewardResponseDTO response = customerTranscationService.calculateRewardPoints(customerId);
	    return ResponseEntity.ok(response);
	}
	
	
	 /**
     * Endpoint to fetch the monthly reward points for a specific customer.
     * Logs the request and delegates the reward point retrieval to the service layer.
     * @param customerId the ID of the customer whose monthly reward points are to be fetched
     * @return a Map where keys are month names and values are reward points for each month
     */
	@GetMapping("/{customerId}/monthly")
	public Map<String, Integer> getMonthlyRewardPoints(@PathVariable Long customerId) {
		log.info("Received request to fetch monthly reward points for customerId: {}", customerId);
		return customerTranscationService.getMonthlyRewardPoints(customerId);
	}
}
