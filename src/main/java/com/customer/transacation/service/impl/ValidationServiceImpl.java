package com.customer.transacation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.transacation.repository.CustomerTranscationRepository;
import com.customer.transaction.errors.CustomerTransactionException;

@Service
public class ValidationServiceImpl {

    @Autowired
    private CustomerTranscationRepository customerTranscationRepository;

	/*
	 * @throws CustomerTransactionException if the customer ID is invalid
	 */
    public void validateCustomerIdIsPresent(Long customerId) {
        Long existCustomerId = customerTranscationRepository.findCustomerIdByCustomerId(customerId);
        if (existCustomerId == null || existCustomerId <= 0) {
        	throw new CustomerTransactionException("CustomerId not found: " + customerId);
        }
    }

}
