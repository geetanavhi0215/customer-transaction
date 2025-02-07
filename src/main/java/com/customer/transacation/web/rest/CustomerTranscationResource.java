package com.customer.transacation.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.repository.CustomerTranscationRepository;
import com.customer.transacation.service.CustomerTranscationService;

@RestController
public class CustomerTranscationResource {


	private static final Logger LOG = LoggerFactory.getLogger(CustomerTranscationResource.class);

	private static final String ENTITY_NAME = "rewardPointsCalculatorServiceCustomerTranscation";

	@Autowired
	private CustomerTranscationService customerTranscationService;


	public CustomerTranscationResource(CustomerTranscationService customerTranscationService) {
		this.customerTranscationService = customerTranscationService;
	}

	@GetMapping("api/customer-transacation/{customerId}")
	public RewardResponseDTO getRewardPoints(@PathVariable Long customerId) {
		return customerTranscationService.calculateRewardPoints(customerId);
	}
}
